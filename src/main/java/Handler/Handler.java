package Handler;

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

public interface Handler {
    Object Registration(User user);

    User Authorization(User user);

    LinkedList<String> Country();

    LinkedList<Integer> Years();

    LinkedList<String> Categories();

    LinkedList<CategoryValue> Constituent(String constituent);

    LinkedList<CountryImportExport> CountryImportExport(String country);

    LinkedList<CountryCategory> CountryShare(String country, String year);

    LinkedList<UserInfo> AllUsers();

    LinkedList<CountryCategory> CountryConstituent(String country);

    void AddCountyImportExporty(CountryAdd countryAdd);

    void UpdateCountyImportExporty(CountryAdd countryAdd);

    void DeleteCountyImportExporty(CountryAdd countryAdd);

    void DeleteUsers(String id);

    void AddUsers(UserInfo user);

    void UpdateUsers(UserInfo user);

    LinkedList<WorldYearValue> WorldConstituentExport(String country);

    LinkedList<CountryShare> CategoryShareExport(String category, String year);

    LinkedList<CountryShare> CategoryShareImport(String category, String year);

}
