# PopularMovies
To make movie request you have to provide your own API_KEY from https://www.themoviedb.org/ and put it into
app.gradle

android {
  defaultConfig {
    buildConfigField "String", "API_KEY", '"YOUR_API_KEY"'
  }
}

