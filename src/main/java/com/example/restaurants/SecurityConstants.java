package com.example.restaurants;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/app/register";
    public static final String POPULAR_LOCATIONS_URL = "/app/popular-locations";
    public static final String RANDOM_RESTAURANTS_URL = "/app/random";
    public static final String USER_URL = "/app/user";
    public static final String COUNTRIES = "/app/countries";
    public static final String CITIES = "/app/cities";
    public static final String SEARCH = "/app/search";
    public static final String RESTAURANTS_BY_FILTER = "/app/getRestaurantsByFilter";
    public static final String COUSINES = "/app/cousines";
    public static final String ADD_REVIEW = "/app/insertComment";

}