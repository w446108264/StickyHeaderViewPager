package com.stickyheaderviewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.library.StickHeaderScrollViewFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollViewSimpleFragment extends StickHeaderScrollViewFragment {

    public static ScrollViewSimpleFragment newInstance() {
        ScrollViewSimpleFragment fragment = new ScrollViewSimpleFragment();
        return fragment;
    }

    public static ScrollViewSimpleFragment newInstance(String title) {
        ScrollViewSimpleFragment fragment = new ScrollViewSimpleFragment();
        fragment.setTitle(title);
        return fragment;
    }

    @Override
    public ScrollView createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ScrollView)inflater.inflate(R.layout.fragment_scrollview, container, false);
    }

    @Override
    public void bindData() {
        ScrollView scrollView = getScrollView();
        LinearLayout linearLayout = (LinearLayout)scrollView.findViewById(R.id.ly_content);

        TextView tv_title = (TextView)scrollView.findViewById(R.id.tv_title);
        tv_title.setText("沁园春·雪");

        TextView tv_data = (TextView)scrollView.findViewById(R.id.tv_data);
        tv_data.setText("北国风光\n" +
                "千里冰封\n" +
                "万里雪飘\n" +
                "望长城内外\n" +
                "惟余莽莽\n" +
                "大河上下\n" +
                "顿失滔滔\n" +
                "山舞银蛇\n" +
                "原驰蜡象\n" +
                "欲与天公试比高\n" +
                "须晴日\n" +
                "看红装素裹\n" +
                "分外妖娆\n" +
                "\n" +
                "江山如此多娇\n" +
                "引无数英雄竞折腰\n" +
                "惜秦皇汉武\n" +
                "略输文采\n" +
                "唐宗宋祖\n" +
                "稍逊风骚\n" +
                "一代天骄\n" +
                "成吉思汗\n" +
                "只识弯弓射大雕\n" +
                "俱往矣\n" +
                "数风流人物\n" +
                "还看今朝");

        if(callBack != null){
            callBack.bindData();
        }
    }

    CallBack callBack;

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack{
        void bindData();
    }
}
