package com.blankj.utilcode.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/10/02
 *     desc  : utils about bus, and the site of https://github.com/Blankj/StaticBus will help u.
 * </pre>
 */
public final class BusUtils {

    public static <T> T post(String name, Object... objects) {
        if (name == null || name.length() == 0) return null;
        return null;
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface Subscribe {
        String name() default "";
    }

//    public static final class BusActivity extends Activity {
//        public static void start(Activity context, String name, Object... objects) {
//            Intent starter = new Intent(context, BusActivity.class);
//            starter.putExtra("name", name);
//            for (int i = 0; i < objects.length; i++) {
//                Object param = objects[i];
//                if (param instanceof Parcelable) {
//                    starter.putExtra("args" + i, (Parcelable) param);
//                } else if (param instanceof Serializable) {
//                    starter.putExtra("args" + i, (Serializable) param);
//                }
//            }
//            starter.putExtra("args", objects);
//            context.startActivityForResult(starter, 1);
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            Intent intent = getIntent();
//            if (intent == null) {
//                finish();
//                return;
//            }
//            String name = intent.getStringExtra("name");
//            if (name == null || name.equals("")) {
//                finish();
//                return;
//            }
//            List<Object> params = new ArrayList<>();
//            for (int i = 0; i < 1000; i++) {
//                Object extra = intent.getParcelableExtra("args" + i);
//                if (extra == null) {
//                    extra = intent.getSerializableExtra("args" + i);
//                    if (extra == null) {
//                        break;
//                    } else {
//                        params.add(extra);
//                    }
//                } else {
//                    params.add(extra);
//                }
//            }
//            intent.getSerializableExtra("args");
//            Object post = BusUtils.post(name, params.toArray());
//            setResult(1, post);
//            finish();
//        }
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}