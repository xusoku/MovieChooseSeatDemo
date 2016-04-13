package view;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SSThumView extends View
{
  private Bitmap bitmap = null;
  private Paint paint = null;

  public SSThumView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setBitmap(Bitmap paramBitmap)
  {
    this.bitmap = paramBitmap;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
//    Log.i("TAG", "onDraw()...");
    if (this.bitmap != null)
      paramCanvas.drawBitmap(this.bitmap, 0.0F, 0.0F, this.paint);
  }
}