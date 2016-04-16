StickyHeaderViewPager
======================

> j.s 🇨🇳 
> 
> Stop Support 
> 
> recommend the new project 「[w446108264/ScrollableLayout](https://github.com/w446108264/ScrollableLayout) 」(easy to add a headview for any view and supports sticking the navigator on the top when ItemView scrolls.)


An Android library supports sticking the navigator on the top when ItemView scrolls in Viewpager.
 
other library which more freely defined 「 [w446108264/StickHeaderLayout](https://github.com/w446108264/StickHeaderLayout) 」

 
![Art](https://github.com/w446108264/StickyHeaderViewPager/raw/master/output/big.gif)
 
![Art](https://github.com/w446108264/StickyHeaderViewPager/raw/master/output/little.gif)
 

# Features

* Support RecyclerView, ScrollView, WebView, ListView.
* Scrolling, with smooth scrolling fling, will not be interrupt when ticking the navigator.
* Don't need to preset the height value,all is automatic.

# Samples

You can [download a sample APK](https://github.com/w446108264/StickyHeaderViewPager/raw/master/output/simple.apk) 

  
# Usage
    
### Layout
 
howerver, `StickHeaderViewPager` must have two childViews,frist childView will scroll with viewpager,second will tick on the top.

```xml
  <com.library.StickHeaderViewPager
        android:id="@+id/shvp_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/toolbar">
        
        <ImageView
            android:id="@+id/iv_headerImage"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:src="@drawable/bg_simple_header"
            android:scaleType="centerInside"/>

        <TextView
            android:id="@+id/tv_stick"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:gravity="center_vertical"
            android:paddingLeft="10dp"
            android:background="#657232"
            android:text="This is a simple demo!"
            />  
 </com.library.StickHeaderViewPager>
```

### Fragment
 
and then create a fragment which extends `ScrollFragment`.it's easy. if you need a ListView ,please make your fragment extends StickHeaderListFragment,if you want to create a webView ,just extends StickHeaderWebViewFragment. even dot't need to inflate view by yourself. for a example.

```java
  public class ListViewSimpleFragment extends StickHeaderListFragment {

    public static ListViewSimpleFragment newInstance() {
        ListViewSimpleFragment fragment = new ListViewSimpleFragment();
        return fragment;
    }

    public static ListViewSimpleFragment newInstance(String title) {
        ListViewSimpleFragment fragment = new ListViewSimpleFragment();
        fragment.setTitle(title);
        return fragment;
    }

    @Override
    public void bindData() {
        setAdapter();
    }

    public void setAdapter() {
        if (getActivity() == null) return;

        int size = 100;
        String[] stringArray = new String[size];
        for (int i = 0; i < size; ++i) {
            stringArray[i] = ""+i;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringArray);

        getScrollView().setAdapter(adapter);
    }
}
```

### Activity
 
last step,init your StickHeaderViewPager with `StickHeaderViewPagerBuilder`.that's all. by the way,activity should extends FragmentActivity or AppCompatActivity.

```java
StickHeaderViewPagerBuilder.stickTo((StickHeaderViewPager) findViewById(R.id.shvp_content))
                .setFragmentManager(getSupportFragmentManager())
                .addScrollFragments(ListViewSimpleFragment.newInstance("ListView"))
                .notifyData();
```


# Contact & Help

Please fell free to contact me if there is any problem when using the library.

* email: shengjun8486@gmail.com 


# Thanks
* ParallaxHeaderViewPager

