package cn.waycube.wrjy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.activities.CourseDetailsActivity;
import cn.waycube.wrjy.activities.EvaluateActivity;
import cn.waycube.wrjy.activities.ReportActivity;
import cn.waycube.wrjy.base.MyBaseAdapter;
import cn.waycube.wrjy.listenter.ConfirmListenter;
import cn.waycube.wrjy.model.Course;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class CourseAdapter extends MyBaseAdapter {

    private ConfirmListenter listenter;

    public CourseAdapter(Context context, List<? extends Object> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_course, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();


        }
        /**
         * 适配数据
         */
        final Course course = (Course) mData.get(position);

        //名称
        viewHolder.tvName.setText(course.getName());
        //图片
        ImageLoader.getInstance().displayImage(course.getImg(), viewHolder.imageView1);
        //上课时间
        viewHolder.tvTime.setText(course.getClass_time());


        /**
         * 大课
         */
        if (course.getType() == 1) {

            viewHolder.tvSize.setText("  (大课)");

            /*
             *判断老师是否请假
             */
            if (course.getTeacher_leave() == 1) {
                //关闭确认上课
                viewHolder.tvConfirm.setVisibility(View.GONE);
                //关闭评价
                viewHolder.tvEvaluate.setVisibility(View.GONE);
                //关闭查看课程报告
                viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                viewHolder.tvMultipleChoice.setText("老师请假");


            } else {

                switch (course.getStatus()){
                    case  0://待老师签到
                        break;
                    case  1://待开始上课
                        break;
                    case  2://待点名
                        break;
                    case  3://待老师提交上课报告
                        break;
                    case  4://待评价
                        break;
                    default:
                        break;

                }
                //待老师签到
                if (course.getStatus() == 0) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待老师签到");
                }

                //待开始上课
                if (course.getStatus() == 1) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待开始上课");
                }
                //待点名
                if (course.getStatus() == 2) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待点名");
                }
                //待提交报告
                if (course.getStatus() == 3) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待提交报告");
                }

//                //待老师评分
//                if (course.getStatus() == 4) {
//                    //关闭评价
//                    viewHolder.tvEvaluate.setVisibility(View.GONE);
//                    viewHolder.tvConfirm.setVisibility(View.GONE);
//                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
//                    viewHolder.tvMultipleChoice.setText("待老师评分");
//
//
////                //确认上课
////                if (course.getConfirm() == 0) {
////                    viewHolder.tvConfirm.setVisibility(View.VISIBLE);
////                    viewHolder.tvConfirm.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            listenter.confirmListenter(course.getId());
////
////                        }
////                    });
////                } else {
////                    viewHolder.tvConfirm.setVisibility(View.GONE);
////
////                }
//
//                    //是否可以查看课程报告
//                    if (course.getReport() == 0) {
//                        viewHolder.tvEvaluateReport.setVisibility(View.GONE);
//                    } else {
//                        viewHolder.tvEvaluateReport.setVisibility(View.VISIBLE);
//                    }
//
//                    //查看课程报告
//                    viewHolder.tvEvaluateReport.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext, ReportActivity.class);
//                            intent.putExtra(KeyUtil.KEY_ONE, course.getCid());
//                            intent.putExtra(KeyUtil.KEY_TWO, course.getType());
//                            mContext.startActivity(intent);
//                        }
//                    });
//                }
                //评价
                if (course.getStatus() == 4) {
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //提示
                    viewHolder.tvMultipleChoice.setVisibility(View.GONE);


//                //确认上课
//                if (course.getConfirm() == 0) {
//                    viewHolder.tvConfirm.setVisibility(View.VISIBLE);
//                    viewHolder.tvConfirm.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            listenter.confirmListenter(course.getId());
//
//                        }
//                    });
//                } else {
//                    viewHolder.tvConfirm.setVisibility(View.GONE);
//
//                }

                    //是否可以查看课程报告
                    if (course.getReport() == 0) {
                        viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    } else {
                        viewHolder.tvEvaluateReport.setVisibility(View.VISIBLE);
                    }

                    //查看课程报告
                    viewHolder.tvEvaluateReport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ReportActivity.class);
                            intent.putExtra(KeyUtil.KEY_ONE, course.getId());
                            intent.putExtra(KeyUtil.KEY_TWO, course.getType());
                            mContext.startActivity(intent);
                        }
                    });

                    if (course.getIs_comment()==0){
                        //评价
                        viewHolder.tvEvaluate.setVisibility(View.VISIBLE);
                        viewHolder.tvEvaluate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, EvaluateActivity.class);
                                intent.putExtra(KeyUtil.KEY_ONE, course.getId());
                                mContext.startActivity(intent);
                            }
                        });

                    }else {
                        viewHolder.tvEvaluate.setVisibility(View.GONE);

                    }

                }
            }
        } else {

            //小课
            viewHolder.tvSize.setText("  (小课)");

              /*
             *判断老师是否请假
             */
            if (course.getTeacher_leave() == 1) {
                //关闭确认上课
                viewHolder.tvConfirm.setVisibility(View.GONE);
                //关闭评价
                viewHolder.tvEvaluate.setVisibility(View.GONE);
                //关闭查看课程报告
                viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                viewHolder.tvMultipleChoice.setText("老师请假");


            } else {

                //待老师签到
                if (course.getStatus() == 0) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待老师确认");
                }

                //待开始上课
                if (course.getStatus() == 1) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待老师签到");
                }
                //待点名
                if (course.getStatus() == 2) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待开始上课");
                }
                //待提交报告
                if (course.getStatus() == 3) {
                    //关闭确认上课
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    //关闭评价
                    viewHolder.tvEvaluate.setVisibility(View.GONE);
                    //关闭查看课程报告
                    viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    viewHolder.tvConfirm.setVisibility(View.GONE);
                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
                    viewHolder.tvMultipleChoice.setText("待提交报告");
                }
//                //待老师评分
//                if (course.getStatus() == 4) {
//                    //关闭评价
//                    viewHolder.tvEvaluate.setVisibility(View.GONE);
//
//                    viewHolder.tvMultipleChoice.setVisibility(View.VISIBLE);
//                    viewHolder.tvMultipleChoice.setText("待老师评分");
//
//                    //确认上课
//                    if (course.getConfirm() == 0) {
//                        viewHolder.tvConfirm.setVisibility(View.VISIBLE);
//                        viewHolder.tvConfirm.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                listenter.confirmListenter(course.getId());
//
//                            }
//                        });
//                    } else {
//                        viewHolder.tvConfirm.setVisibility(View.GONE);
//
//                    }
//                    //是否可以查看课程报告
//                    if (course.getReport() == 0) {
//                        viewHolder.tvEvaluateReport.setVisibility(View.GONE);
//                    } else {
//                        viewHolder.tvEvaluateReport.setVisibility(View.VISIBLE);
//                    }
//                    //查看课程报告
//                    viewHolder.tvEvaluateReport.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext, ReportActivity.class);
//                            intent.putExtra(KeyUtil.KEY_ONE, course.getCid());
//                            intent.putExtra(KeyUtil.KEY_TWO, course.getType());
//                            mContext.startActivity(intent);
//                        }
//                    });
//
//                }

                //评价
                if (course.getStatus() == 4) {

                    //提示语
                    viewHolder.tvMultipleChoice.setVisibility(View.GONE);


                    //确认上课
                    if (course.getConfirm() == 0) {
                        viewHolder.tvConfirm.setVisibility(View.VISIBLE);
                        viewHolder.tvConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listenter.confirmListenter(course.getId());

                            }
                        });
                    } else {
                        viewHolder.tvConfirm.setVisibility(View.GONE);

                    }


                    //是否可以查看课程报告
                    if (course.getReport() == 0) {
                        viewHolder.tvEvaluateReport.setVisibility(View.GONE);
                    } else {
                        viewHolder.tvEvaluateReport.setVisibility(View.VISIBLE);
                    }

                    //查看课程报告
                    viewHolder.tvEvaluateReport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ReportActivity.class);
                            intent.putExtra(KeyUtil.KEY_ONE, course.getId());
                            intent.putExtra(KeyUtil.KEY_TWO, course.getType());
                            mContext.startActivity(intent);
                        }
                    });

                    if (course.getIs_comment()==0){
                        //评价
                        viewHolder.tvEvaluate.setVisibility(View.VISIBLE);
                        viewHolder.tvEvaluate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, EvaluateActivity.class);
                                intent.putExtra(KeyUtil.KEY_ONE, course.getId());
                                mContext.startActivity(intent);
                            }
                        });

                    }else {
                        viewHolder.tvEvaluate.setVisibility(View.GONE);
                    }
                }
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                intent.putExtra(KeyUtil.KEY_ONE, course.getCid());
                intent.putExtra(KeyUtil.KEY_TWO, course.getId());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }


    /**
     * 事件外派
     */
    public void setListenter(ConfirmListenter listenter) {
        this.listenter = listenter;
    }

    static class ViewHolder {
        @Bind(R.id.imageView1)
        RoundedImageView imageView1;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_size)
        TextView tvSize;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_confirm)
        TextView tvConfirm;
        @Bind(R.id.tv_evaluate)
        TextView tvEvaluate;
        @Bind(R.id.tv_multiple_choice)
        TextView tvMultipleChoice;
        @Bind(R.id.tv_affirm_report)
        TextView tvAffirmReport;
        @Bind(R.id.tv_evaluate_report)
        LinearLayout tvEvaluateReport;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
