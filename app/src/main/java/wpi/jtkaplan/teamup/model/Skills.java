package wpi.jtkaplan.teamup.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Member model
 */
public class Skills extends RelationalElement<Student, Class> {

    /*A RelationalElement describes the RELATION between a
     *   User (param0)
     *   Object (param1)
     *wherein the user uses the object.
     * For our case, a Skill is a student's abilities for a class
     * */

    //user's skills in this class
    private HashMap<String, Integer> skills = new HashMap<String, Integer>();

    @Exclude
    DatabaseReference dbr = null;

    public Skills(Student s, Class c) {
        super(s, c);
        updateRTDB();
    }

    public Skills() {
        super();
    }

    @Override
    public final String loc() {
        return "Skills";
    }

    @Override
    public void getAsync(String uid, ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(this.getUID()).child(uid);
        child.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getAsync(ValueEventListener valueEventListener) {
        DatabaseReference child = db.get().child(this.getUID()).child(this.getUID());
        child.addListenerForSingleValueEvent(valueEventListener);
    }


    public void addSkill(String skill, Integer skillLevel) {
        this.skills.put(skill, skillLevel);
        updateRTDB();
    }

    public void removeSkill(String s) {
        this.skills.remove(s);
        updateRTDB();
    }

    public HashMap<String, Integer> getSkills() {
        return skills;
    }

    @Exclude
    public void setSkills(HashMap<String, Integer> skills) {
        this.skills = skills;
        updateRTDB();
    }
}
