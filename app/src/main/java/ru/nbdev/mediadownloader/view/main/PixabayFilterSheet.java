package ru.nbdev.mediadownloader.view.main;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.common.MapList;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;

public class PixabayFilterSheet {

    private BottomSheetDialog filterDialog;
    private Spinner orderFilterSpinner;
    private Spinner imageTypeFilterSpinner;
    private Spinner categoryFilterSpinner;

    private MapList<PixabayFilter.Order, String> orderSpinnerMap;
    private MapList<PixabayFilter.ImageType, String> imageTypeSpinnerMap;
    private MapList<PixabayFilter.Category, String> categorySpinnerMap;

    public PixabayFilterSheet(Activity activity) {
        View bottomSheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_main, null);
        orderFilterSpinner = bottomSheet.findViewById(R.id.spinner_order);
        imageTypeFilterSpinner = bottomSheet.findViewById(R.id.spinner_image_type);
        categoryFilterSpinner = bottomSheet.findViewById(R.id.spinner_image_category);

        filterDialog = new BottomSheetDialog(activity);
        filterDialog.setContentView(bottomSheet);
        orderSpinnerInit(activity);
        imageTypeSpinnerInit(activity);
        categorySpinnerInit(activity);
    }

    private void orderSpinnerInit(Context context) {
        orderSpinnerMap = new MapList<>();
        orderSpinnerMap.put(PixabayFilter.Order.POPULAR, context.getString(R.string.filter_popular));
        orderSpinnerMap.put(PixabayFilter.Order.LATEST, context.getString(R.string.filter_latest));

        ArrayAdapter<String> orderFilterAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                orderSpinnerMap.valuesList()
        );
        orderFilterSpinner.setAdapter(orderFilterAdapter);
    }

    private void imageTypeSpinnerInit(Context context) {
        imageTypeSpinnerMap = new MapList<>();
        imageTypeSpinnerMap.put(PixabayFilter.ImageType.ALL, context.getString(R.string.filter_all));
        imageTypeSpinnerMap.put(PixabayFilter.ImageType.PHOTO, context.getString(R.string.filter_photo));
        imageTypeSpinnerMap.put(PixabayFilter.ImageType.ILLUSTRATION, context.getString(R.string.filter_illustration));
        imageTypeSpinnerMap.put(PixabayFilter.ImageType.VECTOR, context.getString(R.string.filter_vector));

        ArrayAdapter<String> imageTypeFilterAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                imageTypeSpinnerMap.valuesList()
        );
        imageTypeFilterSpinner.setAdapter(imageTypeFilterAdapter);
    }

    private void categorySpinnerInit(Context context) {
        categorySpinnerMap = new MapList<>();
        categorySpinnerMap.put(PixabayFilter.Category.ALL, context.getString(R.string.filter_all));
        categorySpinnerMap.put(PixabayFilter.Category.FASHION, context.getString(R.string.filter_fashion));
        categorySpinnerMap.put(PixabayFilter.Category.NATURE, context.getString(R.string.filter_nature));
        categorySpinnerMap.put(PixabayFilter.Category.BACKGROUNDS, context.getString(R.string.filter_backgrounds));
        categorySpinnerMap.put(PixabayFilter.Category.SCIENCE, context.getString(R.string.filter_science));
        categorySpinnerMap.put(PixabayFilter.Category.EDUCATION, context.getString(R.string.filter_education));
        categorySpinnerMap.put(PixabayFilter.Category.PEOPLE, context.getString(R.string.filter_people));
        categorySpinnerMap.put(PixabayFilter.Category.FEELINGS, context.getString(R.string.filter_feelings));
        categorySpinnerMap.put(PixabayFilter.Category.RELIGION, context.getString(R.string.filter_religion));
        categorySpinnerMap.put(PixabayFilter.Category.HEALTH, context.getString(R.string.filter_health));
        categorySpinnerMap.put(PixabayFilter.Category.PLACES, context.getString(R.string.filter_places));
        categorySpinnerMap.put(PixabayFilter.Category.ANIMALS, context.getString(R.string.filter_animals));
        categorySpinnerMap.put(PixabayFilter.Category.INDUSTRY, context.getString(R.string.filter_industry));
        categorySpinnerMap.put(PixabayFilter.Category.FOOD, context.getString(R.string.filter_food));
        categorySpinnerMap.put(PixabayFilter.Category.COMPUTER, context.getString(R.string.filter_computer));
        categorySpinnerMap.put(PixabayFilter.Category.SPORTS, context.getString(R.string.filter_sports));
        categorySpinnerMap.put(PixabayFilter.Category.TRANSPORTATION, context.getString(R.string.filter_transportation));
        categorySpinnerMap.put(PixabayFilter.Category.TRAVEL, context.getString(R.string.filter_travel));
        categorySpinnerMap.put(PixabayFilter.Category.BUILDINGS, context.getString(R.string.filter_buildings));
        categorySpinnerMap.put(PixabayFilter.Category.BUSINESS, context.getString(R.string.filter_business));
        categorySpinnerMap.put(PixabayFilter.Category.MUSIC, context.getString(R.string.filter_music));

        ArrayAdapter<String> categoryFilterAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                categorySpinnerMap.valuesList()
        );
        categoryFilterSpinner.setAdapter(categoryFilterAdapter);
    }

    public void show(PixabayFilter filter) {
        if (filter != null) {
            orderFilterSpinner.setSelection(orderSpinnerMap.indexOfKey(filter.getOrder()));
            imageTypeFilterSpinner.setSelection(imageTypeSpinnerMap.indexOfKey(filter.getType()));
            categoryFilterSpinner.setSelection(categorySpinnerMap.indexOfKey(filter.getCategory()));
        }
        filterDialog.show();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        filterDialog.setOnDismissListener(dialog -> {
                    PixabayFilter.Order order = orderSpinnerMap.keyAt(orderFilterSpinner.getSelectedItemPosition());
                    PixabayFilter.ImageType type = imageTypeSpinnerMap.keyAt(imageTypeFilterSpinner.getSelectedItemPosition());
                    PixabayFilter.Category category = categorySpinnerMap.keyAt(categoryFilterSpinner.getSelectedItemPosition());
                    PixabayFilter filter = new PixabayFilter(order, type, category);
                    listener.onDismiss(filter);
                }
        );
    }

    public interface OnDismissListener {
        void onDismiss(PixabayFilter filter);
    }
}
