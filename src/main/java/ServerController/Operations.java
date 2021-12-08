package ServerController;

import Handler.Handler;
import com.example.alltrade.model.category.CategoryValue;
import com.example.alltrade.model.category.WorldYearValue;
import com.example.alltrade.model.country.CountryAdd;
import com.example.alltrade.model.country.CountryCategory;
import com.example.alltrade.model.country.CountryImportExport;
import com.example.alltrade.model.country.CountryShare;
import com.example.alltrade.model.user.CurrentUser;
import com.example.alltrade.model.user.User;
import com.example.alltrade.model.user.UserInfo;

import java.util.ArrayList;
import java.util.LinkedList;

public class Operations {
    public static User GetAuthorization(User user, Handler handler) { return handler.Authorization(user); }

    public static Object GetRegistration(User user, Handler handler) { return handler.Registration(user); }

    public static LinkedList<String> GetListOfCountry(Handler handler) { return handler.Country(); }

    public static LinkedList<Integer> GetListOfYears(Handler handler) { return handler.Years(); }

    public static LinkedList<String> GetListOfCategory(Handler handler)
    {
        return handler.Categories();
    }

    public static LinkedList<CountryImportExport> GetCountryImportExport(String country, Handler handler) { return handler.CountryImportExport(country); }

    public static void AddCommonImportExport(CountryAdd countryAdd, Handler handler) { handler.AddCountyImportExporty(countryAdd); }

    public static void UpdateCommonImportExport(CountryAdd countryAdd, Handler handler) { handler.UpdateCountyImportExporty(countryAdd); }

    public static void DeleteCommonImportExport(CountryAdd countryAdd, Handler handler) { handler.DeleteCountyImportExporty(countryAdd); }

    public static LinkedList<CountryCategory> GetCountryShare(String country, String year, Handler handler) { return handler.CountryShare(country, year); }

    public static LinkedList<UserInfo> GetAllUsers(Handler handler) {return handler.AllUsers(); }

    public static void DeleteUsers(String id, Handler handler) { handler.DeleteUsers(id); }

    public static void AddUsers(UserInfo user, Handler handler) { handler.AddUsers(user); }

    public static void UpdateUsers(UserInfo user, Handler handler) { handler.UpdateUsers(user); }

    public static LinkedList<CategoryValue> GetConstituent(String category, Handler handler) { return handler.Constituent(category); }

    public static LinkedList<WorldYearValue> GetWorldConstituentExport(String country, Handler handler) { return handler.WorldConstituentExport(country); }

    public static LinkedList<CountryShare> GetCategoryShareExport(String category, String year, Handler handler) { return handler.CategoryShareExport(category, year); }

    public static LinkedList<CountryShare> GetCategoryShareImport(String category, String year, Handler handler) { return handler.CategoryShareImport(category, year); }

    public static LinkedList<CountryCategory> GetCountryConstituentDB(String country, Handler handler) { return handler.CountryConstituent(country); }

}
