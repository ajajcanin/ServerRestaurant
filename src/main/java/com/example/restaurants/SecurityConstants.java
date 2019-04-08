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
    public static final String CITIES_PAGINATION = "/app/citiesPagination";
    public static final String COUSINES_PAGINATION = "/app/cousinesPagination";
    public static final String USERS_PAGINATION = "/app/usersPagination";
    public static final String EXTRAS = "/app/getExtraDetails";
    public static final String COUNTERS = "/app/counters";
    public static final String IS_ADMIN = "/app/isAdmin";


    public static final String ADD_RESTAURANT = "/app/addRestaurant";
    public static final String EDIT_RESTAURANT = "/app/editRestaurant";
    public static final String DELETE_RESTAURANT = "/app/deleteRestaurant";


    public static final String ADD_CATEOGRY = "/app/addCategory";
    public static final String EDIT_CATEOGRY = "/app/editCategory";
    public static final String DELETE_CATEOGRY = "/app/deleteCategory";

    public static final String ADD_LOCATION = "/app/addLocation";
    public static final String EDIT_LOCATION = "/app/editLocation";
    public static final String DELETE_LOCATION = "/app/deleteLocation";

    public static final String ADD_USER = "/app/addUser";
    public static final String EDIT_USER = "/app/editUser";
    public static final String DELETE_USER = "/app/deleteUser";


}