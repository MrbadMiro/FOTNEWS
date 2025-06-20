package com.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private LinearLayout profileContainer;
    private ImageView ivProfile, ivDropdownArrow;
    private ImageView ivSports, ivEvents, ivAcademic;
    private LinearLayout newsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initViews();
        setupClickListeners();

        // Load academic news by default
        loadNewsContent("academic");
    }

    private void initViews() {
        profileContainer = findViewById(R.id.profile_container);
        ivProfile = findViewById(R.id.iv_profile);
        ivDropdownArrow = findViewById(R.id.iv_dropdown_arrow);

        // Footer category icons
        ivSports = findViewById(R.id.iv_sports);
        ivEvents = findViewById(R.id.iv_events);
        ivAcademic = findViewById(R.id.iv_academic);

        // News container
        newsContainer = findViewById(R.id.news_container);
    }

    private void setupClickListeners() {
        // Profile dropdown click listener
        profileContainer.setOnClickListener(v -> showProfileDropdown());

        // Category icon click listeners
        ivSports.setOnClickListener(v -> loadNewsContent("sports"));
        ivEvents.setOnClickListener(v -> loadNewsContent("events"));
        ivAcademic.setOnClickListener(v -> loadNewsContent("academic"));
    }

    private void showProfileDropdown() {
        // Inflate custom popup layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_menu_layout, null);

        // Create PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set background
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dropdown_background));
        popupWindow.setElevation(8);

        // Set click listeners
        TextView menuUserInfo = popupView.findViewById(R.id.menu_user_info);
        TextView menuDevInfo = popupView.findViewById(R.id.menu_dev_info);

        menuUserInfo.setOnClickListener(v -> {
            startActivity(new Intent(NewsActivity.this, UserInfoActivity.class));
            popupWindow.dismiss();
        });

        menuDevInfo.setOnClickListener(v -> {
            startActivity(new Intent(NewsActivity.this, DevInfoActivity.class));
            popupWindow.dismiss();
        });

        // Show popup below profile container
        popupWindow.showAsDropDown(profileContainer, 0, 0);
    }

    private void loadNewsContent(String category) {
        // Clear existing content
        newsContainer.removeAllViews();

        // Get news data based on category
        List<NewsData.NewsItem> newsItems;
        String categoryTitle;

        switch (category) {
            case "sports":
                newsItems = NewsData.getSportsNews();
                categoryTitle = "Sports News";
                break;
            case "events":
                newsItems = NewsData.getEventsNews();
                categoryTitle = "Events News";
                break;
            case "academic":
            default:
                newsItems = NewsData.getAcademicNews();
                categoryTitle = "Academic News";
                break;
        }

        // Add category title
        addCategoryTitle(categoryTitle);

        // Use map-like function to create news items
        for (NewsData.NewsItem newsItem : newsItems) {
            addNewsCard(newsItem);
        }
    }

    private void addCategoryTitle(String title) {
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(20);
        titleView.setTextColor(getResources().getColor(R.color.primary_red));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 48); // 16dp bottom margin
        titleView.setLayoutParams(params);

        newsContainer.addView(titleView);
    }

    private void addNewsCard(NewsData.NewsItem newsItem) {
        // Inflate the news card layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.news_card_item, newsContainer, false);

        // Find views in the card
        TextView categoryBadge = cardView.findViewById(R.id.tv_category);
        TextView titleView = cardView.findViewById(R.id.tv_title);
        TextView timestampView = cardView.findViewById(R.id.tv_timestamp);
        ImageView imageView = cardView.findViewById(R.id.iv_news_image);
        TextView descriptionView = cardView.findViewById(R.id.tv_description);

        // Set data using map-like approach
        categoryBadge.setText(capitalizeFirst(newsItem.getCategory()));
        titleView.setText(newsItem.getTitle());
        timestampView.setText(newsItem.getTimestamp());
        descriptionView.setText(newsItem.getDescription());

        // Set image resource dynamically
        int imageResId = getResources().getIdentifier(
                newsItem.getImageResource(),
                "drawable",
                getPackageName()
        );
        if (imageResId != 0) {
            imageView.setImageResource(imageResId);
        }

        // Add margin to card
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 48); // 16dp bottom margin
        cardView.setLayoutParams(params);

        // Add card to container
        newsContainer.addView(cardView);
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.length() == 0) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
