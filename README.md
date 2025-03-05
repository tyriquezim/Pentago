This an Android application for the board game, Pentago. The application makes use of a wide range of Views including the RecyclerView to display achievements.
It utilises View binding to allow the use of Views declared in XML in a type safe and efficient manner.
The application also makes use of the Android Room library to achieve app persistence between different runs of the app and makes use of kotlinx serialisation to convert kotlin objects into strings that can be stored in the database.
Lastly, the app uses coroutines to perform database operations, improving the apps responsiveness.
