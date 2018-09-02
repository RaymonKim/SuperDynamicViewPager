# SuperDynamicViewPager

SuperDynamicViewPager :
  - ViewPager with dynamic width or height!
  
# Features
  - You can set width or height of ViewPager based on its child's maximum height.
  - You can set size of ViewPager with ratio.

# ScreenShot
<div>.
  <img width="500" src="https://user-images.githubusercontent.com/26247304/44955858-12c63400-aef5-11e8-8ebc-058fac8f020f.png"/>
</div>
  
# How To Use
[![](https://jitpack.io/v/RaymonKim/SuperDynamicViewPager.svg)](https://jitpack.io/#RaymonKim/SuperDynamicViewPager)
----------------------------
	dependencies {
	        implementation 'com.github.RaymonKim:SuperDynamicViewPager:1.0'
	}

# Sample
    <raymon.superdynamicviewpager.SuperDynamicViewPager
        android:id="@+id/pager_wrap"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:pagerMode="wrap"/>

    <raymon.superdynamicviewpager.SuperDynamicViewPager
        android:id="@+id/pager_ratio"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:pagerMode="ratio"
        app:pagerRatio="1.8"/>

  Attributes
  - Mode : Ratio, Wrap
  - Ratio : Float
