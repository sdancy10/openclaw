package ai.openclaw.android.gateway

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DebugLog {
  private const val MAX_LINES = 50
  private val _lines = MutableStateFlow<List<String>>(emptyList())
  val lines: StateFlow<List<String>> = _lines.asStateFlow()

  fun log(tag: String, message: String) {
    val timestamp = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.US).format(java.util.Date())
    val entry = "$timestamp [$tag] $message"
    android.util.Log.d("DebugLog", entry)
    synchronized(this) {
      val current = _lines.value.toMutableList()
      current.add(entry)
      if (current.size > MAX_LINES) current.removeAt(0)
      _lines.value = current
    }
  }

  fun clear() {
    _lines.value = emptyList()
  }
}
