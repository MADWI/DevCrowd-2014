package pl.devcrowd.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TmpListActivity extends Activity {

	private ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tmp_list_layout);
		
		list = (ListView) findViewById(R.id.listView);
		ArrayList<AgendaItem> array = new ArrayList<AgendaItem>();
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowt temat", "Jan Kowalski"));
		
		AgendatItemsAdapter adapter = new AgendatItemsAdapter(this, R.layout.agenda_item_layout, array);
		list.setAdapter(adapter);
	}
	

}
