package eventApp.model;

public class StudentPreferences {
    public boolean preferMusicEvents;
    public boolean preferTheatreEvents;
    public boolean preferMovieEvents;
    public boolean preferSportsEvents;
    public boolean preferDanceEvents;


    /**
     * Updates the student's preferences based on a comma-separated string of event types.
     * Resets all preferences to false before applying the new ones.
     * Valid event types are MUSIC, THEATRE, DANCE, MOVIE, SPORTS.
     * A maximum of 3 preferences can be set at a time.
     *
     * @param studentRawStringPreferences a comma-separated string of preferred event types
     * @return true if preferences were updated successfully, false if more than 3 were provided
     */
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
