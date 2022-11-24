package com.ruiz.emovie.util.constants

object Constants {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_AUTHENTICATE = "https://www.themoviedb.org/authenticate/"
    const val BASE_URL_IMAGES = "https://image.tmdb.org/t/p/w1280/"
    const val API_KEY = "92066bc9407736b8df41fb748fa1c827"
    const val API_LENGUAGE = "es-ES"

    const val CONNECTION_TIMEOUT = 15L
    const val READ_TIMEOUT = 15L

    const val SPLASH_TIMEOUT = 5000

    const val MOVIES_DATABASE = "movies_database"
    const val MOVIE_GENRES_DB_TABLE = "movie_genres_db_table"
    const val MOVIE_GENRES_KEYS_DB_TABLE = "movie_genres_keys_db_table"
    const val MOVIE_UPCOMING_DB_TABLE = "movie_upcoming_db_table"
    const val MOVIE_UPCOMING_KEYS_DB_TABLE = "movie_upcoming_keys_db_table"
    const val MOVIE_TOP_RATED_DB_TABLE = "movie_top_rated_db_table"
    const val MOVIE_TOP_RATED_KEYS_DB_TABLE = "movie_top_rated_keys_db_table"
    const val MOVIE_POPULAR_DB_TABLE = "movie_popular_db_table"
    const val MOVIE_POPULAR_KEYS_DB_TABLE = "movie_popular_keys_db_table"
    const val MOVIE_FAVORITES_DB_TABLE = "movie_favorites_db_table"
    const val MOVIE_FAVORITES_KEYS_DB_TABLE = "movie_favorites_keys_db_table"

    const val TYPE_MOVIE_UPCOMING = "upcoming"
    const val TYPE_MOVIE_TOP = "top"
    const val TYPE_MOVIE_POPULAR = "popular"

    const val VIDEO_SITE = "YouTube"
    const val VIDEO_TYPE = "Trailer"

    const val NOTIFICATION_CHANNEL_ID = "12345"
    const val NOTIFICATION_CHANNEL_NAME = "Locations"
    const val NOTIFICATION_ID = 12345

    const val ACTION_START_LOCATION_SERVICE = "start_location_service"
    const val ACTION_STOP_LOCATION_SERVICE = "stop_location_service"

    const val LOCATION_INTERVAL = 300000L
    const val LOCATION_INTERVAL_FASTEST = 150000L

    const val MEDIA_TYPE_MOVIE = "movie"
}