package pl.devcrowd.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AgendaDetailsActivity extends Activity {

	private RelativeLayout topicCard;
	private RelativeLayout prelegentCard;
	private RelativeLayout rateCard;
	
	private TextView textTopic;
	private TextView textHour;
	private TextView textTopicDetails;
	
	private TextView textPrelegent;
	private TextView textPrelegentDetails;
	
	private RatingBar ratingBar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda_details_layout);
		
		topicCard = (RelativeLayout) findViewById(R.id.topicCard);
		prelegentCard = (RelativeLayout) findViewById(R.id.prelegentCard);
		rateCard = (RelativeLayout) findViewById(R.id.rateCard);
		
		textTopic = (TextView) findViewById(R.id.textTopic);
		textHour = (TextView) findViewById(R.id.textHour);
		textTopicDetails = (TextView) findViewById(R.id.textTopicDetails);
		
		textPrelegent = (TextView) findViewById(R.id.textPrelegent);
		textPrelegentDetails = (TextView) findViewById(R.id.textPrelegentDetails);
		
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		
		topicCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (textTopicDetails.getVisibility() == View.GONE) {
							textTopicDetails.setVisibility(View.VISIBLE);
						} else if (textTopicDetails.getVisibility() == View.VISIBLE) {
							textTopicDetails.setVisibility(View.GONE);
						}
					}
				});
			}
		});
		
		prelegentCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (textPrelegentDetails.getVisibility() == View.GONE) {
							textPrelegentDetails.setVisibility(View.VISIBLE);
						} else if (textPrelegentDetails.getVisibility() == View.VISIBLE) {
							textPrelegentDetails.setVisibility(View.GONE);
						}
					}
				});
				
			}
		});
		
		ratingBar.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					AlertDialog dialog = buildRateDialog();
					dialog.show();
				}
				return true;
			}
		});
		
	}
	
	private AlertDialog buildRateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.ratePresentation))
			.setMessage("Simply content...")
			.setNegativeButton(getString(R.string.cancel), null)
			.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(AgendaDetailsActivity.this, "Rate was send!", Toast.LENGTH_SHORT).show();
				}
			});
		
		return builder.create();
		
	}

}
