package wpi.jtkaplan.teamup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class ClassesRecyclerViewFragment extends Fragment {

    private List<Class> classes;
    private RecyclerView rv;
    private User user;
    private ClassesRVAdapter adapter;

    String loc;
    String uid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        rv = view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return view;
    }

    private void initializeData() {
        // TODO: Need to get this data from Google Cloud for each user.
        classes = new ArrayList<Class>();

        uid = UserPreferences.read(UserPreferences.UID_VALUE, null);
        loc = UserPreferences.read(UserPreferences.LOC_VALUE, null);

        User.getUserFromPref(uid, loc, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (loc.equals("Professors")) {
                    user = dataSnapshot.getValue(Professor.class);
                    loadClasses();
                } else if (loc.equals("Students")){
                    user = dataSnapshot.getValue(Student.class);
                    loadClasses();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadClasses(){
        user.getClassesAsync(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Adding class");
                System.out.print(dataSnapshot.getValue());
                wpi.jtkaplan.teamup.model.Class toAdd = dataSnapshot.getValue(wpi.jtkaplan.teamup.model.Class.class);
                adapter.addClass(toAdd);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void initializeAdapter() {
        adapter = new ClassesRVAdapter(classes);
        adapter.getItemId(classes.size() - 1);
        rv.setAdapter(adapter);
    }
}