# PopularMovies
**To make movie request** You have to provide your own MOVIE_API_KEY from https://www.themoviedb.org/ and put it into
    app.gradle
    
   
**To play trailers** you have to provide your own YOUTUBE_API_KEY from
 https://developers.google.com/youtube/android/player/register and put it into
    app.gradle

android {
  defaultConfig {
    buildConfigField "String", "API_KEY", '"MOVIE_API_KEY"'
    buildConfigField "String", "YOUTUBE_API_KEY", '"YOUTUBE_API_KEY"'
  }
}

