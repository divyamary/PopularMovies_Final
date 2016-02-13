package in.divyamary.moviereel.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Transformation used to extract {@link Palette} information from the {@linkplain Bitmap}.
 * From https://gist.github.com/imminent/d35ad752f657bc695722- Gist for a modified approach to
 * integrating Palette with Picasso proposed by Jake Wharton
 */
public final class PaletteTransformation implements Transformation {
    private static final PaletteTransformation INSTANCE = new PaletteTransformation();
    private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

    private PaletteTransformation() {
    }

    private static Palette getPalette(Bitmap bitmap) {
        return CACHE.get(bitmap);
    }

    /**
     * Obtains a {@link PaletteTransformation} to extract {@link Palette} information.
     *
     * @return A {@link PaletteTransformation}
     */
    public static PaletteTransformation instance() {
        return INSTANCE;
    }

    //# Transformation Contract
    @Override
    public final Bitmap transform(Bitmap source) {
        if (!CACHE.containsKey(source)) {
            /*Palette.Builder builder = new Palette.Builder(source);
            builder.maximumColorCount(32);*/
            final Palette palette = Palette.from(source).generate();
            //final Palette palette = builder.generate();
            CACHE.put(source, palette);
        }
        return source;
    }

    @Override
    public String key() {
        return getClass().getCanonicalName() + ":";
    }

    /**
     * A {@link Callback} that receives {@link Palette} information in its callback.
     *
     * @see Callback
     */
    public static abstract class PaletteCallback implements Callback {
        private WeakReference<ImageView> mImageView;

        public PaletteCallback(@NonNull ImageView imageView) {
            mImageView = new WeakReference<ImageView>(imageView);
        }

        protected abstract void onSuccess(Palette palette);

        @Override
        public final void onSuccess() {
            if (getImageView() == null) {
                return;
            }
            final Bitmap bitmap = ((BitmapDrawable) getImageView().getDrawable()).getBitmap();
            final Palette palette = getPalette(bitmap);
            onSuccess(palette);
        }

        private ImageView getImageView() {
            return mImageView.get();
        }
    }
}