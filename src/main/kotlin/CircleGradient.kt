import processing.core.PApplet
import processing.core.PConstants

class CircleGradient : PApplet() {

  companion object Factory {
    fun run() {
      var art = CircleGradient()
      art.setSize(500, 500)
      art.runSketch()
    }
  }

  override fun setup() {
    strokeCap(PConstants.ROUND)
    colorMode(PConstants.HSB, 360f, 100f, 100f, 1.0f)
    ellipseMode(PConstants.CORNER)

    for (x in 0..500 step 5) {
      for (y in 0..500 step 5) {
        val hue = rescale(random(x.toFloat() + y.toFloat(), x + y + 100f), 0f, 500f, 130f, 220f)
        fill(hue, 90f, 90f)
        val size = 5f
        ellipse(x.toFloat(), y.toFloat(), size, size)
      }
    }

  }

  fun rescale(value: Float, oldMin: Float, oldMax: Float, newMin: Float, newMax: Float): Float {
    val oldSpread = oldMax - oldMin;
    val newSpread = newMax - newMin;
    val a = value - oldMin;
    val b = newSpread / oldSpread;
    val c = a * b;
    val d = c + newMin
    return d
  }

  fun drawLine(h: Float) {
    stroke(0f, 255f - h)
    line(10f, h, width - 20f, h)
    stroke(255f, h)
    line(10f, h + 4f, width - 20f, h + 4)
  }

}

fun main(args: Array<String>) {
  CircleGradient.run()
}