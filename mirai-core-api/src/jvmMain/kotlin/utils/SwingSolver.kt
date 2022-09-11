/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.mamoe.mirai.utils

/**
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 */

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@MiraiExperimentalApi
public object SwingSolver : LoginSolver() {
    override val isSliderCaptchaSupported: Boolean get() = true

    public override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String {
        val image = runBIO { ImageIO.read(data.inputStream()) }
        return SwingLoginSolver(
            "Mirai PicCaptcha(${bot.id})",
            "Pic Captcha",
            null,
            topComponent = JLabel(ImageIcon(image)),
        ).openAndWait()
    }

    public override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? = coroutineScope {
        val openWithTxCaptchaHelper = JButton("Open with TxCaptchaHelper")
        val solver = SwingLoginSolver(
            "Mirai SliderCaptcha(${bot.id})",
            "ticket",
            arrayOf(
                "URL", JTextField(url),
                "", openWithTxCaptchaHelper,
            ),
            topComponent = JLabel(
                """
                <html>
                需要滑动验证码, 完成后请输入ticket <br/>
                @see: https://github.com/project-mirai/mirai-login-solver-selenium <br/>
                @see: https://docs.mirai.mamoe.net/mirai-login-solver-selenium/
                """.trimIndent()
            ),
        )

        fun JButton.doClickEvent() = launch {
            val status = JTextField("Requesting...")
            val txhelperSolverConfirmButton = JButton("确定")
            val txhelperSolver = SwingLoginSolver(
                "Mirai SliderCaptcha(${bot.id}) (TxCaptchaHelper)",
                "",
                arrayOf(
                    "", status,
                    "",
                    JButton("Open TxHelperSolver site").onClick {
                        openBrowserOrAlert(
                            "https://github.com/mzdluo123/TxCaptchaHelper",
                            "TxCaptchaHelper",
                            "TxCaptchaHelper",
                            getWindowForComponent(this),
                        )
                    },
                    "", txhelperSolverConfirmButton,
                ),
                hiddenInput = true,
                parentComponent = this@doClickEvent,
                value = status,
            )
            val helper = object : TxCaptchaHelper() {
                override fun onComplete(ticket: String) {
                    txhelperSolver.def.complete(ticket)
                }

                override fun updateDisplay(msg: String) {
                    status.text = msg
                }
            }
            helper.start(this, url)
            txhelperSolver.def.invokeOnCompletion { helper.dispose() }
            solver.def.complete(txhelperSolver.openAndWait().trim())
        }
        openWithTxCaptchaHelper.onClick { doClickEvent() }
        return@coroutineScope solver.openAndWait().takeIf { it.isNotEmpty() }
    }

    @Suppress("DuplicatedCode")
    override suspend fun onSolveDeviceVerification(
        bot: Bot,
        requests: DeviceVerificationRequests
    ): DeviceVerificationResult {
        requests.sms?.let { req ->
            solveSms(bot, req)?.let { return it }
        }
        requests.fallback?.let { fallback ->
            solveFallback(bot, fallback.url)
            return fallback.solved()
        }
        error("User rejected SMS login while fallback login method not available.")
    }

    @Deprecated(
        "Please use onSolveDeviceVerification instead",
        replaceWith = ReplaceWith("onSolveDeviceVerification(bot, url, null)"),
        level = DeprecationLevel.WARNING
    )
    public override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String {
        return solveFallback(bot, url)
    }

    private suspend fun solveSms(bot: Bot, request: DeviceVerificationRequests.SmsRequest): DeviceVerificationResult? =
        coroutineScope {
            val smsRequester = object {
                var lastRequested = 0L

                fun requestSms(parentComponent: Component) = launch {
                    // oh, so shit code

                    val diff = (System.currentTimeMillis() - lastRequested).milliseconds
                    if (diff < 1.minutes) {
                        parentComponent.createTip(
                            """请求过于频繁, 请在 ${1.minutes - diff} 秒后再试""".trimIndent()
                    ).openAndWait()
                    return@launch
                }

                lastRequested = System.currentTimeMillis()
                kotlin.runCatching {
                    request.requestSms()
                }.fold(
                    onSuccess = {
                        parentComponent.createTip(
                            """发送验证码成功, 请注意查收. 若未收到, 可在一分钟后重试.""".trimIndent()
                        ).openAndWait()
                    },
                    onFailure = {
                        parentComponent.createTip(
                            "<html>发送验证码失败.<br/><br/>${
                                it.stackTraceToString().replace("\n", "<br/>").replace("\r", "")
                            }"
                        ).openAndWait()
                    }
                )
            }
        }
        val title = "Mirai Device Verification (${bot.id})"
        val phoneNumber = request.phoneNumber
        val countryCode = request.countryCode
        val phoneNumberTip = if (phoneNumber != null && countryCode != null)
            """(+$countryCode) $phoneNumber"""
        else "(无法获取到手机号码)"


        val code = SwingLoginSolver(
            title, "",
            arrayOf(
                "",
                JButton("发送验证码").onClick {
                    smsRequester.requestSms(this)
                },
                "",
                JLabel("验证码 (输入完成后按回车):")
            ),
            hiddenInput = false,
            topComponent = JLabel(
                """
                    <html>
                    需要进行短信验证码验证<br>
                    一条短信验证码将发送到你的手机 $phoneNumberTip<br>
                    运营商可能会收取正常短信费用<br>
                """.trimIndent()
            )
        ).openAndWait().trim().ifEmpty { return@coroutineScope null }
        request.solved(code)
    }

    private fun Component.createTip(tip: String) = SwingLoginSolver(
        "提示", "",
        arrayOf("", JLabel()),
        hiddenInput = true,
        topComponent = JLabel(tip),
        parentComponent = this,
    )

    private suspend fun solveFallback(bot: Bot, url: String): String {
        val title = "Mirai Device Verification (${bot.id})"
        return SwingLoginSolver(
            title, "",
            arrayOf(
                "", HyperLinkLabel(url, "设备锁验证", title),
                "URL", JTextField(url),
            ),
            hiddenInput = true,
            topComponent = JLabel(
                """
                    <html>
                    需要进行账户安全认证<br>
                    该账户有设备锁/不常用登录地点/不常用设备登录的问题<br>
                    请在<b>手机 QQ</b> 打开下面链接
                    成功后请关闭该窗口
                """.trimIndent()
            )
        ).openAndWait()
    }
}


// 隔离类代码
// 在 jvm 中, 使用 WindowHelperJvm 不会加载 SwingSolverKt
// 不会触发各种 NoDefClassError
internal object WindowHelperJvm {
    enum class PlatformKind {
        ANDROID,
        SWING,
        CLI
    }

    private val logger = MiraiLogger.Factory.create(this::class)

    internal val platformKind: PlatformKind = kotlin.run {
        if (kotlin.runCatching { Class.forName("android.util.Log") }.isSuccess) {
            // Android platform
            return@run PlatformKind.ANDROID
        }
        if (System.getProperty("mirai.no-desktop") != null) return@run PlatformKind.CLI
        kotlin.runCatching {
            Class.forName("java.awt.GraphicsEnvironment")
            if (GraphicsEnvironment.isHeadless()) return@run PlatformKind.CLI

            Class.forName("java.awt.Desktop")
            Class.forName("java.awt.Toolkit")
            Toolkit.getDefaultToolkit()

            if (Desktop.isDesktopSupported()) {
                logger.info(
                    """
                                    Mirai 正在使用桌面环境. 如遇到验证码将会弹出对话框. 可添加 JVM 属性 `mirai.no-desktop` 以关闭.
                                """.trimIndent()
                )
                logger.info(
                    """
                                    Mirai is using desktop. Captcha will be thrown by window popup. You can add `mirai.no-desktop` to JVM properties (-Dmirai.no-desktop) to disable it.
                                """.trimIndent()
                )
                return@run PlatformKind.SWING
            } else {
                return@run PlatformKind.CLI
            }
        }.onFailure { error ->
            if (error.javaClass == ClassNotFoundException::class.java && error.cause == null) return@onFailure
            logger.warning("Failed to initialize module `java.desktop`", error)
        }.getOrElse {
            return@run PlatformKind.CLI
        }
    }
}


internal val windowImage: BufferedImage? by lazy {
    WindowHelperJvm::class.java.getResourceAsStream("project-mirai.png")?.use {
        ImageIO.read(it)
    }
}

internal val windowIcon: Icon? by lazy {
    windowImage?.let(::ImageIcon)
}

/**
 * @param url 打开的链接
 * @param text 显示的提示内容
 * @param fallbackTitle 无法打开链接时的提醒窗口标题
 */
internal class HyperLinkLabel constructor(
    url: String,
    text: String,
    fallbackTitle: String
) : JLabel() {
    init {
        super.setText("<html><a href='$url'>$text</a></html>")
        addMouseListener(object : MouseAdapter() {

            override fun mouseClicked(e: MouseEvent) {
                openBrowserOrAlert(
                    url,
                    "Mirai 无法直接打开浏览器, 请手动复制以下 URL 打开",
                    fallbackTitle,
                    this@HyperLinkLabel
                )
            }
        })
    }
}

internal class SwingLoginSolver(
    title: String?,
    inputType: String?,
    // Array<[inlined] Pair<String, Component>>
    additionInputs: Array<Any>?,
    hiddenInput: Boolean = false,
    topComponent: Component? = null,
    parentComponent: Component? = null,
    val value: JTextField = JTextField("", 15),
) {
    val def = CompletableDeferred<String>()
    val frame: Window = if (parentComponent == null) {
        JFrame(title)
    } else {
        JDialog(JOptionPane.getFrameForComponent(parentComponent), title, true)
    }

    init {
        if (frame is JFrame) {
            frame.iconImage = windowImage
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        }
        frame.minimumSize = Dimension(228, 62)
        frame.layout = BorderLayout(5, 5)
        kotlin.run {
            val contentPane = JPanel()
            // contentPane.background = Color.cyan
            frame.add(contentPane, BorderLayout.PAGE_END)
            val label = JLabel(inputType)
            label.labelFor = value
            val layout = GroupLayout(contentPane)
            contentPane.layout = layout
            layout.autoCreateGaps = true
            layout.autoCreateContainerGaps = true
            kotlin.run {
                val lining = layout.createSequentialGroup()
                val left = layout.createParallelGroup()
                val right = layout.createParallelGroup()
                if (topComponent != null) lining.addComponent(topComponent)
                if (additionInputs != null) {
                    var i = 0
                    while (i < additionInputs.size) {
                        val left0 = JLabel(additionInputs[i].toString())
                        val right0 = additionInputs[i + 1] as Component
                        left0.labelFor = right0
                        left.addComponent(left0)
                        right.addComponent(right0)
                        lining.addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(left0)
                                .addComponent(right0)
                        )
                        i += 2
                    }
                }
                if (!hiddenInput) {
                    left.addComponent(label)
                    right.addComponent(value)
                }
                layout.setHorizontalGroup(
                    layout.createParallelGroup()
                        .also { group ->
                            if (topComponent != null) {
                                group.addComponent(topComponent)
                            }
                        }
                        .addGroup(
                            layout.createSequentialGroup()
                                .addGroup(left)
                                .addGroup(right)
                        )
                )
                if (hiddenInput) {
                    layout.setVerticalGroup(lining)
                } else {
                    layout.setVerticalGroup(
                        lining.addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label)
                                .addComponent(value)
                        )
                    )
                }
            }
        }
        value.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent?) {
                when (e!!.keyCode) {
                    27, 10 -> {
                        def.complete(value.text)
                    }
                }
            }

            override fun keyReleased(e: KeyEvent?) {
            }
        })
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                def.complete(value.text)
            }
        })
    }

    suspend fun openAndWait(): String {
        frame.pack()
        frame.setLocationRelativeTo(null)
        runBIO {
            def.invokeOnCompletion {
                SwingUtilities.invokeLater {
                    frame.dispose()
                }
            }
            frame.isVisible = true
        }
        return def.await()
    }
}

private fun openBrowserOrAlert(
    url: String,
    msg: String,
    title: String,
    component: Component? = null,
) {
    // Try to open browser safely. #694
    try {
        Desktop.getDesktop().browse(URI(url))
    } catch (ex: Exception) {
        JOptionPane.showInputDialog(
            component,
            msg,
            title,
            JOptionPane.WARNING_MESSAGE,
            windowIcon,
            null,
            url
        )
    }
}

private fun <T : Component> T.onClick(onclick: T.(MouseEvent) -> Unit): T = apply {
    addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            onclick(this@onClick, e)
        }
    })
}

private tailrec fun getWindowForComponent(component: Component): Window {
    if (component is Window) return component
    return getWindowForComponent(component.parent ?: error("Component not attached"))
}
