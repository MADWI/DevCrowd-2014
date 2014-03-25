package pl.devcrowd.app.overviews;

import pl.devcrowd.app.utils.DebugLog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {

	private static final String CROPPED_PAINT_COLOR = "#BAB399";

	public RoundImageView(final Context context) {
		super(context);
	}

	public RoundImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundImageView(final Context context, final AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable != null || getWidth() != 0 || getHeight() != 0) {
			Bitmap b = ((BitmapDrawable) drawable).getBitmap();
			Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
			Bitmap roundBitmap = getCroppedBitmap(bitmap, getWidth());
			canvas.drawBitmap(roundBitmap, 0, 0, null);
		} else {
			DebugLog.e("Drawable null or bad width/height");
			return;
		}

	}

	public static Bitmap getCroppedBitmap(final Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		} else {
			sbmp = bmp;
		}
		
		int width = sbmp.getWidth();
		int height = sbmp.getHeight();

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor(CROPPED_PAINT_COLOR));
		canvas.drawCircle(width / 2, height / 2, width / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

}