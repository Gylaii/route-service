package domain

import kotlinx.serialization.Serializable

@Serializable data class Point(val lat: Double, val lng: Double)

@Serializable
data class PointOfInterest(
    val id: String,
    val name: String,
    val location: Point,
    val category: String,
    val description: String? = null,
)

@Serializable
data class Route(
    val id: String,
    val userId: String,
    val startPoint: Point,
    val endPoint: Point,
    val waypoints: List<Point>,
    val pointsOfInterest: List<PointOfInterest>,
    val distance: Double, // в метрах
    val estimatedTime: Int, // в минутах
    val caloriesBurn: Int,
    val createdAt: Long, // timestamp
)
