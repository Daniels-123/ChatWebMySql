package com.dybcatering.chatwebmysql.CrearGrupo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dybcatering.chatwebmysql.AdaptadorUsuarios.CardViewDataAdapter;
import com.dybcatering.chatwebmysql.AdaptadorUsuarios.Student;
import com.dybcatering.chatwebmysql.MainActivity;
import com.dybcatering.chatwebmysql.R;

import java.util.ArrayList;
import java.util.List;

public class CrearGrupoActivity extends AppCompatActivity {

	private Toolbar toolbar;

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	private List<Student> studentList;

	private Button btnSelection;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_grupo);

		//toolbar = findViewById(R.id.toolbar);
		btnSelection = (Button) findViewById(R.id.btn_show);

		studentList = new ArrayList<Student>();





		for (int i = 1; i <= 15; i++) {
			Student st = new Student("Student " + i, "androidstudent" + i
					+ "@gmail.com", false);

			studentList.add(st);
		}


		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		// create an Object for Adapter
		mAdapter = new CardViewDataAdapter(studentList);

		// set the adapter object to the Recyclerview
		mRecyclerView.setAdapter(mAdapter);

		btnSelection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String data = "";
				List<Student> stList = ((CardViewDataAdapter) mAdapter)
						.getStudentist();

				for (int i = 0; i < stList.size(); i++) {
					Student singleStudent = stList.get(i);
					if (singleStudent.isSelected() == true) {

						data = data + "\n" + singleStudent.getName().toString();

					}

				}

				Toast.makeText(CrearGrupoActivity.this,
						"Selected Students: \n" + data, Toast.LENGTH_LONG)
						.show();
			}
		});

	}



}
