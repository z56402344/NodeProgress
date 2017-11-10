# NodeProgress  带节点的渐变色进度条
```
1.可以设置渐变色的起始和结束颜色
2.通过设置每段线的长度控制节点数
3.可控制最后节点的位置

## XML
```

 <com.nodeprogress.CustomNodeProgressBar
        android:id="@+id/mPb"
        style="?android:attr/progressBarStyleHorizontal"
        android:layout_width="match_parent"
        android:layout_height="17dp"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_marginTop="20dp"
        android:progressDrawable="@drawable/progress_horizontal"
        android:visibility="visible"
        android_custom:node_pb_bg_color="#99000000"
        android_custom:node_pb_bg_radian="200dp"
        android_custom:node_pb_bg_second_color="#e1e6e8"
        android_custom:node_pb_foreground_color_end="#ff9c29"
        android_custom:node_pb_foreground_color_start="#ffea00"
        android_custom:node_pb_line_width="30"
        android_custom:node_pb_max="150"
        android_custom:node_pb_padding="2dp"
        android_custom:node_pb_radian="200dp"
        android_custom:node_pb_space_margRight="30"/>


![Image][1]

[1]: http://7xs7n9.com1.z0.glb.clouddn.com/device-2017-11-09-113854.gif