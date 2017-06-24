import processing.core.PApplet
import processing.core.PConstants

data class Circle(val radius: Float, val x: Float, val y: Float, var color: FloatArray = floatArrayOf(0f, 0f, 0f))

class CirclePacking : PApplet() {
  companion object Factory {
    fun run() {
      var art = CirclePacking()
      art.setSize(500, 500)
      art.runSketch()
    }
  }

  val circleSizeCounts = listOf(
          65 to 7,
          37 to 17,
          20 to 37,
          7 to 167,
          3 to 301
  )

  fun randomXY(xMin: Float, xMax: Float, yMin: Float, yMax: Float): Pair<Float, Float> {
    return Pair(random(xMin, xMax), random(yMin, yMax))
  }

  override fun setup() {
    strokeCap(PConstants.ROUND)
    colorMode(PConstants.HSB, 360f, 100f, 100f, 1.0f)
    ellipseMode(PConstants.CENTER)

    val allCircles = mutableListOf<Circle>()

    for (circleSizeCount in circleSizeCounts) {
      val circleSize = circleSizeCount.first
      val circleCount = circleSizeCount.second
      for (i in 1..circleCount) {
        // allow up to 100 collisions
        for (c in 0..1000) {
          // generate random point
          // do not allow circles to overlap canvas
          // val (x, y) = randomXY(0f+circleSize, 500f-circleSize, 0f+circleSize, 500f-circleSize);
          // allow circles overlapping canvas
          val (x, y) = randomXY(0f, 500f, 0f, 500f);
          val testCircle = Circle(circleSize.toFloat(), x, y)
          if (!checkCircleCollision(allCircles, testCircle)) {
            // get random color
            val color = weightedChoice(
                    listOf(
                            floatArrayOf(0f, 0f, random(90f, 100f)) to 0.6f,
                            floatArrayOf(random(180f, 220f), 50f, 50f) to 0.3f,
                            floatArrayOf(random(0f, 20f), 80f, 80f) to 0.1f
                    )
            )
            testCircle.color = color
            allCircles.add(testCircle)
            break
          }
        }
      }
    }

    noStroke()
    for (circle in allCircles) {
//      println("drawing circle:${circle}")
      val circleColor = circle.color
      fill(circleColor[0], circleColor[1], circleColor[2])
      ellipse(circle.x, circle.y, circle.radius * 2, circle.radius * 2)
    }

  }

  fun checkCircleCollision(allCircles: List<Circle>, testCircle: Circle): Boolean {
    return allCircles.asSequence().any {
      val distance = dist(it.x, it.y, testCircle.x, testCircle.y)
      distance <= (it.radius + testCircle.radius)
    }
  }

  fun weightedChoice(colorsAndWeights: List<Pair<FloatArray, Float>>): FloatArray {
    val weightSum = colorsAndWeights.sumBy { (it.second * 100).toInt() }
    if (weightSum != 100) throw AssertionError("Weights should sum to 1")
    val random = random(0f, 1.0f)
    var weightTotal = 0f
    for (i in colorsAndWeights) {
      if (random >= weightTotal && random <= weightTotal + i.second) {
        return i.first
      }
      weightTotal += i.second;
    }
    throw Exception("Should have returned a Weighted Choice...")
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
}

fun main(args: Array<String>) {
  CirclePacking.run()
}

