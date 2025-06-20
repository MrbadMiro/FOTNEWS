package com.fotnews;

import java.util.ArrayList;
import java.util.List;

public class NewsData {

    public static class NewsItem {
        private String title;
        private String description;
        private String timestamp;
        private String imageResource;
        private String category;

        public NewsItem(String title, String description, String timestamp, String imageResource, String category) {
            this.title = title;
            this.description = description;
            this.timestamp = timestamp;
            this.imageResource = imageResource;
            this.category = category;
        }

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getTimestamp() { return timestamp; }
        public String getImageResource() { return imageResource; }
        public String getCategory() { return category; }
    }

    public static List<NewsItem> getAcademicNews() {
        List<NewsItem> academicNews = new ArrayList<>();

        academicNews.add(new NewsItem(
                "Final Exam Schedule Released",
                "The final examination schedule for semester 1 has been released. Students can check their exam dates on the student portal and prepare accordingly.",
                "December 15, 2025 • 2:30 PM",
                "academic1",
                "academic"
        ));

        academicNews.add(new NewsItem(
                "New Library Resources Available",
                "The university library has added new digital resources and study materials. Students can access these resources 24/7 through the online portal.",
                "December 14, 2025 • 10:15 AM",
                "academic2",
                "academic"
        ));

        academicNews.add(new NewsItem(
                "Research Symposium 2025",
                "Annual research symposium showcasing student and faculty research projects. Join us to explore innovative solutions and academic excellence.",
                "December 13, 2024 • 9:00 AM",
                "academic3",
                "academic"
        ));

        return academicNews;
    }

    public static List<NewsItem> getEventsNews() {
        List<NewsItem> eventsNews = new ArrayList<>();

        eventsNews.add(new NewsItem(
                "Cultural Festival 2025",
                "Join us for the annual cultural festival featuring music, dance, and art exhibitions. Registration is now open for all participants.",
                "December 20, 2025 • 6:00 PM",
                "event1",
                "events"
        ));

        eventsNews.add(new NewsItem(
                "Tech Innovation Summit",
                "Explore the latest in technology and innovation. Industry experts will share insights on emerging trends and career opportunities.",
                "December 18, 2025 • 1:30 PM",
                "event2",
                "events"
        ));

        eventsNews.add(new NewsItem(
                "Student Orientation Week",
                "Welcome new students to our university community. Learn about campus facilities, academic programs, and student services.",
                "December 16, 2025 • 8:00 AM",
                "event3",
                "events"
        ));

        return eventsNews;
    }

    public static List<NewsItem> getSportsNews() {
        List<NewsItem> sportsNews = new ArrayList<>();

        sportsNews.add(new NewsItem(
                "Annual Sports Meet 2024",
                "The annual sports meet will be held featuring track and field events, team sports, and individual competitions. All students are encouraged to participate.",
                "December 22, 2025 • 7:00 AM",
                "sports1",
                "sports"
        ));

        sportsNews.add(new NewsItem(
                "Basketball Championship Finals",
                "The inter-faculty basketball championship reaches its climax. Come support your teams in the final showdown at the main gymnasium.",
                "December 19, 2024 • 4:00 PM",
                "sports2",
                "sports"
        ));

        sportsNews.add(new NewsItem(
                "Swimming Pool Renovation Complete",
                "The university swimming pool renovation is now complete with new facilities and equipment. Swimming classes and training sessions will resume next week.",
                "December 17, 2025 • 11:30 AM",
                "sports3",
                "sports"
        ));

        return sportsNews;
    }

    public static List<NewsItem> getAllNews() {
        List<NewsItem> allNews = new ArrayList<>();
        allNews.addAll(getAcademicNews());
        allNews.addAll(getEventsNews());
        allNews.addAll(getSportsNews());
        return allNews;
    }
}
