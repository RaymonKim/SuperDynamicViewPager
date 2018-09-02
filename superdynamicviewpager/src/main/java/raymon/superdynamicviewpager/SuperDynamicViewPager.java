package raymon.superdynamicviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Raymon on 2018-08-23.
 */

/**
 * Supports Followings...
 * 1. Calculate and Set Width or Height or Both Width and Height Using Specified Ratio.
 * 2. Calculate and Set Width or Height Using Width or Height of Its Child.
 */
public class SuperDynamicViewPager extends ViewPager {
    private final int PAGER_MODE_DEFAULT    = -1;
    private final int PAGER_MODE_WRAP       = 0;
    private final int PAGER_MODE_RATIO      = 1;

    private ChildMeasurer mChildMeasurer;
    private RatioMeasurer mRatioMeasurer;
    private int mPagerMode = PAGER_MODE_DEFAULT;
    private float mPagerRatio = 1.0f;

    public SuperDynamicViewPager(@NonNull Context context) {
        this(context, null);
    }

    public SuperDynamicViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperDynamicViewPager);
            try {
                int N = typedArray.getIndexCount();
                for (int i = 0; i < N; i++) {
                    int attr = typedArray.getIndex(i);

                    if (attr == R.styleable.SuperDynamicViewPager_pagerMode) {
                        mPagerMode = typedArray.getInteger(attr, PAGER_MODE_DEFAULT);
                    } else if (attr == R.styleable.SuperDynamicViewPager_pagerRatio) {
                        mPagerRatio = typedArray.getFloat(attr, 1.0f);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
        mChildMeasurer = new ChildMeasurer();
        mRatioMeasurer = new RatioMeasurer(mPagerRatio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        switch (mPagerMode) {
            case PAGER_MODE_WRAP:
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                mChildMeasurer.measure(widthMeasureSpec, heightMeasureSpec);
                super.onMeasure(mChildMeasurer.getMeasuredWidth(), mChildMeasurer.getMeasuredHeight());
                break;
            case PAGER_MODE_RATIO:
                mRatioMeasurer.measure(widthMeasureSpec, heightMeasureSpec);
                super.onMeasure(mRatioMeasurer.getMeasuredWidth(), mRatioMeasurer.getMeasuredHeight());
                break;
            default:
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                break;
        }
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }

    private class RatioMeasurer {
        private double mRatio;
        private Integer mMeasuredWidth = null;
        private Integer mMeasuredHeight = null;


        private RatioMeasurer(double ratio) {
            this.mRatio = ratio;
        }

        private void measure(int widthMeasureSpec, int heightMeasureSpec) {
            measure(widthMeasureSpec, heightMeasureSpec, mRatio);
        }

        private void measure(int widthMeasureSpec, int heightMeasureSpec, double aspectRatio) {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = widthMode == View.MeasureSpec.UNSPECIFIED ? Integer.MAX_VALUE : View.MeasureSpec.getSize(widthMeasureSpec);
            int heightMode= View.MeasureSpec.getMode(heightMeasureSpec);
            int heightSize= heightMode == View.MeasureSpec.UNSPECIFIED ? Integer.MAX_VALUE : View.MeasureSpec.getSize(heightMeasureSpec);

            /**
             * Case 1: Both Width & Height Fixed
             */
            if(heightMode == View.MeasureSpec.EXACTLY && widthMode == View.MeasureSpec.EXACTLY) {
                mMeasuredWidth = widthSize;
                mMeasuredHeight = heightSize;

            }
            /**
             * Case 2: Width Dynamic, Height Fixed
             */
            else if(heightMode == View.MeasureSpec.EXACTLY) {
                mMeasuredWidth = (int)Math.min(widthSize, widthSize/aspectRatio);
                mMeasuredHeight = (int)(mMeasuredWidth * aspectRatio);
            }
            /**
             * Case 3: Width Fixed, Height Dynamic
             */
            else if(widthMode == View.MeasureSpec.EXACTLY) {
                mMeasuredHeight = (int)Math.min(heightSize, widthSize/aspectRatio);
                mMeasuredWidth = (int)(mMeasuredHeight * aspectRatio);
            }
            /**
             * Case 4: Both Width & Height Dynamic
             */
            else {
                if(widthSize > heightSize * aspectRatio) {
                    mMeasuredHeight = heightSize;
                    mMeasuredWidth = (int)(mMeasuredHeight * aspectRatio);
                } else {
                    mMeasuredWidth = widthSize;
                    mMeasuredHeight = (int)(mMeasuredWidth / aspectRatio);
                }
            }
        }

        private int getMeasuredWidth() {
            if(mMeasuredWidth == null) {
                throw new IllegalStateException("\n You Need to Run :\n     measure()\n Before Accessing Measured Height");
            }
            return MeasureSpec.makeMeasureSpec(mMeasuredWidth, MeasureSpec.EXACTLY);
        }

        private int getMeasuredHeight() {
            if(mMeasuredHeight == null) {
                throw new IllegalStateException("\n You Need to Run :\n     measure()\n Before Accessing Measured Height");
            }
            return MeasureSpec.makeMeasureSpec(mMeasuredHeight, MeasureSpec.EXACTLY);
        }
    }

    private class ChildMeasurer {
        private Integer mMeasuredWidth = null;
        private Integer mMeasuredHeight = null;

        private void measure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = widthMode == View.MeasureSpec.UNSPECIFIED ? Integer.MAX_VALUE : View.MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = heightMode == View.MeasureSpec.UNSPECIFIED ? Integer.MAX_VALUE : View.MeasureSpec.getSize(heightMeasureSpec);

            /**
             * Case 1: Both Width & Height Fixed
             */
            if (heightMode == View.MeasureSpec.EXACTLY && widthMode == View.MeasureSpec.EXACTLY) {
                mMeasuredWidth = widthSize;
                mMeasuredHeight = heightSize;
            }
            /**
             * Case 2: Width Dynamic, Height Fixed
             */
            else if (heightMode == View.MeasureSpec.EXACTLY) {
                int maxWidth = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    View vRoot = getChildAt(i);
                    vRoot.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec);

                    if (vRoot instanceof ConstraintLayout) {
                        ConstraintLayout vChild = (ConstraintLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredWidth();
                            if (childWidth > maxWidth) maxWidth = childWidth;
                        }
                    } else if (vRoot instanceof LinearLayout) {
                        LinearLayout vChild = (LinearLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredWidth();
                            if (childWidth > maxWidth) maxWidth = childWidth;
                        }
                    } else if (vRoot instanceof RelativeLayout) {
                        RelativeLayout vChild = (RelativeLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredWidth();
                            if (childWidth > maxWidth) maxWidth = childWidth;
                        }
                    } else if (vRoot instanceof FrameLayout) {
                        FrameLayout vChild = (FrameLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredWidth();
                            if (childWidth > maxWidth) maxWidth = childWidth;
                        }
                    }
                }
                mMeasuredWidth = maxWidth;
                mMeasuredHeight = heightSize;
            }
            /**
             * Case 3: Width Fixed, Height Dynamic
             */
            else if (widthMode == View.MeasureSpec.EXACTLY) {
                int maxHeight = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    View vRoot = getChildAt(i);
                    vRoot.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                    if (vRoot instanceof ConstraintLayout) {
                        ConstraintLayout vChild = (ConstraintLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childHeight = vChild.getChildAt(j).getMeasuredHeight();
                            if (childHeight > maxHeight) maxHeight = childHeight;
                        }
                    } else if (vRoot instanceof LinearLayout) {
                        LinearLayout vChild = (LinearLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredHeight();
                            if (childWidth > maxHeight) maxHeight = childWidth;
                        }
                    } else if (vRoot instanceof RelativeLayout) {
                        RelativeLayout vChild = (RelativeLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredHeight();
                            if (childWidth > maxHeight) maxHeight = childWidth;
                        }
                    } else if (vRoot instanceof FrameLayout) {
                        FrameLayout vChild = (FrameLayout) vRoot;
                        for (int j = 0; j < vChild.getChildCount(); j++) {
                            int childWidth = vChild.getChildAt(j).getMeasuredHeight();
                            if (childWidth > maxHeight) maxHeight = childWidth;
                        }
                    }
                }
                mMeasuredWidth = widthSize;
                mMeasuredHeight = maxHeight;
            }
            /**
             * Case 4: Both Width & Height Dynamic
             */
            else {
                throw new IllegalStateException("\n You Need to Use :\n     Mode : Ratio\n To Apply Wrap_Content To Both Width & Height");
            }
        }
        private int getMeasuredWidth() {
            if(mMeasuredWidth == null) {
                throw new IllegalStateException("\n You Need to Run :\n     measure()\n Before Accessing Measured Height");
            }
            return MeasureSpec.makeMeasureSpec(mMeasuredWidth, MeasureSpec.EXACTLY);
        }

        private int getMeasuredHeight() {
            if(mMeasuredHeight == null) {
                throw new IllegalStateException("\n You Need to Run :\n     measure()\n Before Accessing Measured Height");
            }
            return MeasureSpec.makeMeasureSpec(mMeasuredHeight, MeasureSpec.EXACTLY);
        }
    }
}
