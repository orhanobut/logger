package com.orhanobut.logger

/** Frames messages with optional thread and caller information. */
open class PrettyFormatStrategy private constructor(
  private val methodCount: Int,
  private val methodOffset: Int,
  private val showThreadInfo: Boolean,
  private val logStrategy: LogStrategy,
  private val tag: String?,
) : FormatStrategy {
  override fun log(priority: Int, tag: String?, message: String) {
    val outputTag = if (!tag.isNullOrEmpty() && this.tag != tag) "${this.tag}-$tag" else this.tag
    logStrategy.log(priority, outputTag, TOP_BORDER)
    logHeaderContent(priority, outputTag)
    if (methodCount > 0) logStrategy.log(priority, outputTag, MIDDLE_BORDER)

    // Keep UTF-8 code points intact at Android's byte-based log entry limit.
    val bytes = message.toByteArray(Charsets.UTF_8)
    var start = 0
    if (bytes.isEmpty()) logContent(priority, outputTag, "")
    while (start < bytes.size) {
      var end = minOf(start + CHUNK_SIZE, bytes.size)
      if (end < bytes.size) {
        while ((bytes[end].toInt() and 0xC0) == 0x80) end--
      }
      logContent(priority, outputTag, String(bytes, start, end - start, Charsets.UTF_8))
      start = end
    }
    logStrategy.log(priority, outputTag, BOTTOM_BORDER)
  }

  private fun logHeaderContent(priority: Int, tag: String?) {
    if (showThreadInfo) {
      logStrategy.log(priority, tag, "│ Thread: ${Thread.currentThread().name}")
      logStrategy.log(priority, tag, MIDDLE_BORDER)
    }
    if (methodCount <= 0) return
    val trace = Thread.currentThread().stackTrace
    // Android may prepend VMStack frames; begin after the logging call chain.
    val caller = trace.indexOfLast {
      it.className == Thread::class.java.name ||
          it.className == PrettyFormatStrategy::class.java.name ||
          it.className == LoggerPrinter::class.java.name ||
          it.className == AndroidLogAdapter::class.java.name ||
          it.className == DiskLogAdapter::class.java.name ||
          it.className == Logger::class.java.name
    } + 1
    val start = (caller + methodOffset).coerceAtLeast(0)
    val count = minOf(methodCount, trace.size - start)
    var indent = ""
    for (index in (start + count - 1) downTo start) {
      if (index !in trace.indices) continue
      val frame = trace[index]
      val className = frame.className.substringAfterLast('.')
      logStrategy.log(priority, tag,
          "│ $indent$className.${frame.methodName}  (${frame.fileName}:${frame.lineNumber})")
      indent += "   "
    }
  }

  private fun logContent(priority: Int, tag: String?, chunk: String) {
    // Java's split dropped trailing empty lines; preserve that output convention.
    val lines = chunk.split(NEW_LINE).toMutableList()
    if (chunk.isNotEmpty()) while (lines.lastOrNull() == "") lines.removeAt(lines.lastIndex)
    for (line in lines) logStrategy.log(priority, tag, "│ $line")
  }

  open class Builder internal constructor() {
    private var methodCount = 2
    private var methodOffset = 0
    private var showThreadInfo = true
    private var logStrategy: LogStrategy? = null
    private var tag: String? = "PRETTY_LOGGER"

    open fun methodCount(value: Int): Builder = apply { methodCount = value }
    open fun methodOffset(value: Int): Builder = apply { methodOffset = value }
    open fun showThreadInfo(value: Boolean): Builder = apply { showThreadInfo = value }
    open fun logStrategy(value: LogStrategy?): Builder = apply { logStrategy = value }
    open fun tag(tag: String?): Builder = apply { this.tag = tag }
    open fun build(): PrettyFormatStrategy = PrettyFormatStrategy(
        methodCount, methodOffset, showThreadInfo, logStrategy ?: LogcatLogStrategy(), tag)
  }

  companion object {
    private const val CHUNK_SIZE = 4000
    private val NEW_LINE = System.getProperty("line.separator") ?: "\n"
    private val TOP_BORDER = "┌" + "─".repeat(112)
    private val BOTTOM_BORDER = "└" + "─".repeat(112)
    private val MIDDLE_BORDER = "├" + "┄".repeat(112)

    @JvmStatic fun newBuilder(): Builder = Builder()
  }
}
