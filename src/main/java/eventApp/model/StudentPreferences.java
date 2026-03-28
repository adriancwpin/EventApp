package eventApp.model;

public class StudentPreferences {
    public boolean preferMusicEvents;
    public boolean preferTheatreEvents;
    public boolean preferMovieEvents;
    public boolean preferSportsEvents;
    public boolean preferDanceEvents;

    public boolean updatePreference(String studentRawStringPreferences){
        //initalise all preference to false
        preferMusicEvents = false;
        preferTheatreEvents = false;
        preferMovieEvents = false;
        preferSportsEvents = false;
        preferDanceEvents = false;

        //split the input by comma
        String [] parts = studentRawStringPreferences.split(",");

        //Validate the preferences
        if(parts.length > 3){
            return false;
        }

        for(String part: parts){
            String preference = part.trim().toUpperCase();

            switch(preference){
                case "MUSIC":
                    preferMusicEvents = true;
                    break;

                case "THEATRE":
                    preferTheatreEvents = true;
                    break;

                case "MOVIE":
                    preferMovieEvents = true;
                    break;

                case "SPORTS":
                    preferSportsEvents = true;
                    break;

                case "DANCE":
                    preferDanceEvents = true;
                    break;

                default:
                    break; //invalid preferences
            }
        }
        return true;
    }
}
