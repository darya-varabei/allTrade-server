package DataBaseHandlers;

import ConnectionManager.ConnectionManager;
import Handler.Handler;
import Hashing.JBCrypt;
import Literals.DBLiterals;
import com.example.alltrade.model.category.CategoryValue;
import com.example.alltrade.model.category.WorldYearValue;
import com.example.alltrade.model.country.*;
import com.example.alltrade.model.user.CurrentUser;
import com.example.alltrade.model.user.User;
import com.example.alltrade.model.user.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class DBHandler implements Handler {
    private Connection dbConnection;

    public DBHandler() {
        try {
            String connectionString = DBLiterals.connectionString;
            dbConnection = DriverManager.getConnection(connectionString, DBLiterals.login, DBLiterals.password);
        }
        catch (SQLException e) {

        }
    }

    public Object Registration(User user) {
        var countOfUsers = 0;
        var listOfUsers = new ArrayList<User>();
        var getUsers =  "SELECT ul_login FROM UsersLogin " +
                "WHERE ul_login = ?";

        try {
            var request = dbConnection.prepareStatement(getUsers);
            request.setString(1, user.getLogin());
            var table = request.executeQuery();

            while (table.next()) {
                countOfUsers++;
            }
        }
        catch (Exception e) { }

        if (countOfUsers != 0)
        {
            return  "User exists";
        }

        var getRegistration =   "INSERT UsersLogin (ul_login, ul_password, ul_role) " +
                "VALUES (?, ?, ?)";

        try {
            var i = 0;
            var request = dbConnection.prepareStatement(getRegistration);
            request.setString(1, user.getLogin());
            request.setString(2, JBCrypt.hashpw(user.getPassword(), JBCrypt.gensalt()));
            request.setString(3, user.getRole());
            request.executeUpdate();
        }
        catch (Exception e) { }

        var getRegistrationFull =   "INSERT Users (u_email, u_codeCountry, u_dateLastAutorization) " +
                "VALUES (?, 'CAN', ?)";

        try {
            var request = dbConnection.prepareStatement(getRegistrationFull);
            //request.setString(1, user.getEmail());
            request.setString(1, LocalDate.now().toString());
            request.executeUpdate();
        }
        catch (Exception e) { }

        var getUserCode =   "SELECT (ul_codeUser) FROM UsersLogin " +
                "WHERE ul_login = ?";

        try {
            var request = dbConnection.prepareStatement(getUserCode);
            request.setString(1, user.getLogin());
            var table = request.executeQuery();

            table.next();

            //user.setId(table.getInt(1));
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return user;
    }

    public User Authorization(User user) {
        var listOfUsers = new ArrayList<User>();
        var getAuthorization =  "SELECT ul_password, u_codeUser, u_email, co_codeCountry, co_name, co_area, co_population, u_dateLastAutorization, u_dateLastExit, ul_role FROM Users " +
                "INNER JOIN UsersLogin ON Users.u_codeUser = UsersLogin.ul_codeUser " +
                "INNER JOIN Country ON Users.u_codeCountry = Country.co_codeCountry " +
                "WHERE UsersLogin.ul_login = ?";

        var setDate =   "UPDATE Users " +
                "SET u_dateLastAutorization = ? " +
                "WHERE u_codeUser = ?";

        try {
            var request = dbConnection.prepareStatement(getAuthorization);
            request.setString(1, user.getLogin());
            var table = request.executeQuery();

            while (table.next()) {
                CurrentUser candidateOnInter;
                var country = new Country();

                if (!JBCrypt.checkpw(user.getPassword(), table.getString(1)))
                {
                    return null;
                }

                user.setRole(table.getString(10));

                //candidateOnInter.setCodeUser(table.getInt(2));
//                candidateOnInter.setLogin(user.getLogin());
//                candidateOnInter.setPassword(user.getPassword());
//                candidateOnInter.setRole(user.getRole());
                //candidateOnInter.setEmail(table.getString(3));
               // candidateOnInter.setCountry(table.getString(4));

                //country.setId(table.getString(4));
                country.setName(table.getString(5));
                country.setArea(table.getDouble(6));
                country.setPopulation(table.getInt(7));

//                candidateOnInter.setCountry(country);

                //candidateOnInter.setLastAccessDate(table.getString(8));
                //candidateOnInter.setDateLastExit(table.getString(9));

                request = dbConnection.prepareStatement(setDate);
                request.setString(1, LocalDate.now().toString());
                request.setInt(2, table.getInt(2));
                request.executeUpdate();

                return user;
            }
        }
        catch (Exception e) { }

        return null;
    }

    public LinkedList<String> Country() {
        var listOfCountry = new LinkedList<String>();
        var getCountry = "SELECT co_name FROM Country";

        try {
            var request = dbConnection.prepareStatement(getCountry);
            var table = request.executeQuery();

            while (table.next()) {
                listOfCountry.add(table.getString(1));
            }
        }
        catch (Exception e) { }

        return listOfCountry;
    }

    public LinkedList<Integer> Years() {
        var listOfYears = new LinkedList<Integer>();
        var getYears = "SELECT y_year FROM Year";

        try {
            var request = dbConnection.prepareStatement(getYears);
            var table = request.executeQuery();

            while (table.next()) {
                listOfYears.add(table.getInt(1));
            }
        }
        catch (Exception e) { }

        return listOfYears;
    }

    public LinkedList<String> Categories() {
        var listOfCategories = new LinkedList<String>();
        var getCategories = "SELECT ca_category FROM Category";

        try {
            var request = dbConnection.prepareStatement(getCategories);
            var table = request.executeQuery();

            while (table.next()) {
                listOfCategories.add(table.getString(1));
            }

            System.out.println(listOfCategories);
        }
        catch (Exception e) { }

        return listOfCategories;
    }

    public LinkedList<CountryImportExport> CountryImportExport(String country) {
        var listOfImportExport = new LinkedList<CountryImportExport>();

        var countryCode = GetCodeCountry(country);

        var getExport = "SELECT y_year, ce_totalExport FROM CommonExport " +
                "INNER JOIN Country ON Country.co_codeCountry = CommonExport.ce_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CommonExport.ce_codeYear " +
                "WHERE Country.co_codeCountry = ?";

        var getImport = "SELECT ci_totalImport FROM CommonImport " +
                "INNER JOIN Country ON Country.co_codeCountry = CommonImport.ci_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CommonImport.ci_codeYear " +
                "WHERE Country.co_codeCountry = ?";

        try {
            var requestExport = dbConnection.prepareStatement(getExport);
            requestExport.setString(1, countryCode);
            var tableExport = requestExport.executeQuery();

            var requestImport = dbConnection.prepareStatement(getImport);
            requestImport.setString(1, countryCode);
            var tableImport = requestImport.executeQuery();

            while (tableExport.next() && tableImport.next()) {
                var year = tableExport.getInt(1);
                var exportRow = tableExport.getDouble(2);
                var importRow = tableImport.getDouble(1);
                var netExportValue = exportRow - importRow;

                var importExport = new CountryImportExport(year, exportRow, importRow, netExportValue);

                listOfImportExport.add(importExport);
            }
        }
        catch (Exception e) { }
        System.out.println(
                listOfImportExport
        );

        return listOfImportExport;
    }

    public LinkedList<WorldYearValue> WorldConstituentExport(String country) {
        var list = new LinkedList<WorldYearValue>();

        var getExport = "SELECT y_year, SUM(cae_export) FROM " +
                "(SELECT co_name, ca_category, y_year, cae_export FROM CategoryExport " +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryExport.cae_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CategoryExport.cae_codeYear " +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryExport.cae_codeCategory " +
                "WHERE co_name = ?) AS table_1 GROUP BY co_name, y_year";

        try {
            var requestExport = dbConnection.prepareStatement(getExport);
            requestExport.setString(1, country);
            var tableExport = requestExport.executeQuery();

            while (tableExport.next())
            {
                var worldConstituentExport = new WorldYearValue();

                worldConstituentExport.setYear(tableExport.getInt(1));
                worldConstituentExport.setValue(tableExport.getDouble(2));

                list.add(worldConstituentExport);
            }
        }
        catch (Exception e) { }

        return list;
    }

    public LinkedList<CountryCategory> CountryShare(String country, String year){
        var resultList = new LinkedList<CountryCategory>();

        var getExportCountry =  "SELECT cae_export, y_year, ca_category FROM CategoryExport " +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryExport.cae_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CategoryExport.cae_codeYear " +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryExport.cae_codeCategory " +
                "WHERE co_name = ? AND y_year = ?";

        try {
            var requestExportCountry = dbConnection.prepareStatement(getExportCountry);
            requestExportCountry.setString(1, country);
            requestExportCountry.setInt(2, Integer.parseInt(year));
            var tableCountry = requestExportCountry.executeQuery();

            while (tableCountry.next()) {
                var categ = new CountryCategory();

                categ.setImportValue(tableCountry.getDouble(1));
                categ.setYear(tableCountry.getInt(2));
                categ.setCategory(tableCountry.getString(3));

                resultList.add(categ);
            }
        }
        catch (Exception e) { }

        return  resultList;
    }

    public LinkedList<UserInfo> AllUsers(){
        var usersList = new LinkedList<UserInfo>();

        var getUsers =  "SELECT ul_login, ul_password, u_codeUser, u_dateLastAutorization, co_name, ul_role FROM Users " +
                "INNER JOIN UsersLogin ON UsersLogin.ul_codeUser = Users.u_codeUser " +
                "INNER JOIN Country ON Country.co_codeCountry = Users.u_codeCountry";
        try {
            var requestAllUsers = dbConnection.prepareStatement(getUsers);
            var tableUsers = requestAllUsers.executeQuery();

            while (tableUsers.next()) {
                var user = new UserInfo(tableUsers.getString(1), tableUsers.getString(2), String.valueOf(tableUsers.getInt(3)), tableUsers.getString(4), tableUsers.getString(5), tableUsers.getString(6));
                usersList.add(user);
            }

        }catch (Exception e){ }

        return usersList;
    }

    public LinkedList<CountryCategory> CountryConstituent(String country) {
        var listOfCountryConstituent = new LinkedList<CountryCategory>();

        var getImport =     "SELECT y_year, ca_category, cai_import FROM CategoryImport\n" +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryImport.cai_codeCountry\n" +
                "INNER JOIN Year ON Year.y_codeYear = CategoryImport.cai_codeYear\n" +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryImport.cai_codeCategory\n" +
                "WHERE co_name = ?";

        try {
            var requestImport = dbConnection.prepareStatement(getImport);
            requestImport.setString(1, country);
            var tableImport = requestImport.executeQuery();

            while (tableImport.next()) {
                var countryConstituent = new CountryCategory();

                countryConstituent.setYear(tableImport.getInt(1));
                countryConstituent.setCategory(tableImport.getString(2));
                countryConstituent.setExportValue(tableImport.getDouble(3));

                listOfCountryConstituent.add(countryConstituent);
            }
        }
        catch (Exception e) { }

        return listOfCountryConstituent;
    }

    public LinkedList<CategoryValue> Constituent(String constituent) {
        var listOfCountryConstituent = new LinkedList<CategoryValue>();

        var getImport = "SELECT y_year, co_name, cai_import FROM CategoryImport " +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryImport.cai_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CategoryImport.cai_codeYear " +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryImport.cai_codeCategory " +
                "WHERE ca_category = ?";

        var getExport = "SELECT cae_export FROM CategoryExport " +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryExport.cae_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CategoryExport.cae_codeYear " +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryExport.cae_codeCategory " +
                "WHERE ca_category = ?";

        try {
            var requestImport = dbConnection.prepareStatement(getImport);
            requestImport.setString(1, constituent);
            var tableImport = requestImport.executeQuery();

            var requestExport = dbConnection.prepareStatement(getExport);
            requestExport.setString(1, constituent);
            var tableExport = requestExport.executeQuery();

            while (tableImport.next() & tableExport.next()) {
                var exportImportConstituents = new CategoryValue();

                exportImportConstituents.setYear(tableImport.getInt(1));
                exportImportConstituents.setCountry(tableImport.getString(2));
                exportImportConstituents.setImportValue(tableImport.getDouble(3));
                exportImportConstituents.setExportValue(tableExport.getDouble(1));
                exportImportConstituents.setConstituent(constituent);

                listOfCountryConstituent.add(exportImportConstituents);
            }
        }
        catch (Exception e) { }

        return listOfCountryConstituent;
    }

    public void AddCountyImportExporty(CountryAdd countryAdd) {
        Integer codeYear = GetCodeYear(countryAdd.getYear());
        String codeCountry = GetCodeCountry(countryAdd.getCountry());

        if (codeCountry == null)
        {
            var insertCountry = "INSERT INTO Country (co_codeCountry, co_name) VALUES (?, ?);";

            try {
                var requestInsertCountry = dbConnection.prepareStatement(insertCountry);
                requestInsertCountry.setString(1, countryAdd.getCountry().substring(0, 3));
                requestInsertCountry.setString(2, countryAdd.getCountry());

                requestInsertCountry.executeUpdate();

                codeCountry = GetCodeCountry(countryAdd.getCountry());
            }
            catch (Exception e) { }
        }

        var insertExport = "INSERT INTO CommonExport VALUES (?, ?, ?)";
        var insertImport = "INSERT INTO CommonImport VALUES (?, ?, ?)";

        try {
            var requestInsertExport = dbConnection.prepareStatement(insertExport);
            requestInsertExport.setString(1, codeCountry);
            requestInsertExport.setInt(2, codeYear);
            requestInsertExport.setDouble(3, countryAdd.getExportValue());
            requestInsertExport.executeUpdate();

            var requestInsertImport = dbConnection.prepareStatement(insertImport);
            requestInsertImport.setString(1, codeCountry);
            requestInsertImport.setInt(2, codeYear);
            requestInsertImport.setDouble(3, countryAdd.getImportValue());
            requestInsertImport.executeUpdate();
        }
        catch (Exception e) { }
    }

    public void UpdateCountyImportExporty(CountryAdd countryAdd) {
        Integer codeYear = GetCodeYear(countryAdd.getYear());
        String codeCountry = GetCodeCountry(countryAdd.getCountry());

        var updateExport =  "UPDATE CommonExport " +
                "SET ce_totalExport = ? " +
                "WHERE ce_codeCountry = ? AND ce_codeYear = ?";
        var updateImport =  "UPDATE CommonImport " +
                "SET ci_totalImport = ? " +
                "WHERE ci_codeCountry = ? AND ci_codeYear = ?";

        try {
            var requestUpdateExport = dbConnection.prepareStatement(updateExport);
            requestUpdateExport.setDouble(1, countryAdd.getExportValue());
            requestUpdateExport.setString(2, codeCountry);
            requestUpdateExport.setInt(3, codeYear);
            requestUpdateExport.executeUpdate();

            var requestUpdateImport = dbConnection.prepareStatement(updateImport);
            requestUpdateImport.setDouble(1, countryAdd.getImportValue());
            requestUpdateImport.setString(2, codeCountry);
            requestUpdateImport.setInt(3, codeYear);
            requestUpdateImport.executeUpdate();
        }
        catch (Exception e) { }
    }

    public void DeleteCountyImportExporty(CountryAdd countryAdd) {
        Integer codeYear = GetCodeYear(countryAdd.getYear());
        String codeCountry = GetCodeCountry(countryAdd.getCountry());

        var deleteExport =  "DELETE FROM CommonExport " +
                "WHERE ce_codeCountry = ? AND ce_codeYear = ? AND ce_totalExport = ?";
        var deleteImport =  "DELETE FROM CommonImport " +
                "WHERE ci_codeCountry = ? AND ci_codeYear = ? AND ci_totalImport = ?";

        try {
            var requestDeleteExport = dbConnection.prepareStatement(deleteExport);
            requestDeleteExport.setString(1, codeCountry);
            requestDeleteExport.setInt(2, codeYear);
            requestDeleteExport.setDouble(3, countryAdd.getExportValue());
            requestDeleteExport.executeUpdate();

            var requestDeleteImport = dbConnection.prepareStatement(deleteImport);
            requestDeleteImport.setString(1, codeCountry);
            requestDeleteImport.setInt(2, codeYear);
            requestDeleteImport.setDouble(3, countryAdd.getImportValue());
            requestDeleteImport.executeUpdate();
        }
        catch (Exception e) { }
    }

    public void DeleteUsers(String id) {
        var deleteUserLogin = "DELETE FROM UsersLogin WHERE ul_codeUser = ?";
        var deleteUser = "DELETE FROM Users WHERE u_codeUser = 1";

        try {
            var requestUserLogin = dbConnection.prepareStatement(deleteUserLogin);
            requestUserLogin.setInt(1, Integer.parseInt(id));
            requestUserLogin.executeUpdate();

            var requestUser = dbConnection.prepareStatement(deleteUser);
            //requestUser.setInt(1, Integer.parseInt(id));
            requestUser.execute();
        } catch (Exception e) {}
    }

    public void AddUsers(UserInfo user) {
        var countOfUsers = 0;
        var listOfUsers = new ArrayList<User>();
        var getUsers =  "SELECT ul_login FROM UsersLogin " +
                "WHERE ul_login = ?";

        try {
            var request = dbConnection.prepareStatement(getUsers);
            request.setString(1, user.getLogin());
            var table = request.executeQuery();

            while (table.next()) {
                countOfUsers++;
            }
        }
        catch (Exception e) { }

        if (countOfUsers != 0)
        {
            return;
        }

        var getRegistration =   "INSERT UsersLogin (ul_login, ul_password, ul_role) " +
                "VALUES (?, ?, ?)";

        try {
            var i = 0;
            var request = dbConnection.prepareStatement(getRegistration);
            request.setString(1, user.getLogin());
            request.setString(2, JBCrypt.hashpw(user.getPassword(), JBCrypt.gensalt()));
            request.setString(3, user.getRole());
            request.executeUpdate();
        }
        catch (Exception e) { }

        var getRegistrationFull =   "INSERT Users (u_codeCountry) "+
                "VALUES (?)";

        var countryCode = GetCodeCountry(user.getCountry());

        try {
            var i = 0;
            var request = dbConnection.prepareStatement(getRegistrationFull);
            request.setString(1, countryCode);
            request.executeUpdate();
        }
        catch (Exception e) { }
    }

    public void UpdateUsers(UserInfo user) {
        var updateUserLogin =   "UPDATE UsersLogin " +
                "SET ul_login = ?, ul_password = ?, ul_role = ?" +
                "WHERE ul_codeUser = ?";

        var updateUser =    "UPDATE Users " +
                "SET u_codeCountry = ? " +
                "WHERE u_codeUser = ?";
        try {
            var requestUpdateUserLogin = dbConnection.prepareStatement(updateUserLogin);
            requestUpdateUserLogin.setString(1, user.getLogin());
            requestUpdateUserLogin.setString(2, JBCrypt.hashpw(user.getPassword(), JBCrypt.gensalt()));
            requestUpdateUserLogin.setString(3, user.getRole());
            //requestUpdateUserLogin.setInt(4, user.getId());
            requestUpdateUserLogin.executeUpdate();

            var requestUpdateUser = dbConnection.prepareStatement(updateUser);
            requestUpdateUser.setString(1, GetCodeCountry(user.getCountry()));
            requestUpdateUser.executeUpdate();

        }catch (Exception e){ }
    }



    private Integer GetCodeYear(int year) {
        var checkYear = "SELECT y_codeYear FROM Year " +
                "WHERE y_year = ?";

        try {
            var requestСheckYear = dbConnection.prepareStatement(checkYear);
            requestСheckYear.setInt(1, year);
            var tableСheckYear = requestСheckYear.executeQuery();

            while (tableСheckYear.next()) {
                return tableСheckYear.getInt(1);
            }
        }
        catch (Exception e) { }

        return null;
    }

    private String GetCodeCountry(String nameOfCountry) {
        var checkCountry =  "SELECT co_codeCountry FROM Country " +
                "WHERE co_name = ?";

        try {
            var requestСheckCountry = dbConnection.prepareStatement(checkCountry);
            requestСheckCountry.setString(1, nameOfCountry);
            var tableСheckCountry = requestСheckCountry.executeQuery();

            while (tableСheckCountry.next()) {
                return tableСheckCountry.getString(1);
            }
        }
        catch (Exception e) { }

        return null;
    }

    public LinkedList<CountryShare> CategoryShareExport(String category, String year) {
        var list = new LinkedList<CountryShare>();

        var getExport = "SELECT co_name, cae_export FROM CategoryExport " +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryExport.cae_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CategoryExport.cae_codeYear " +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryExport.cae_codeCategory " +
                "WHERE ca_category = ? AND y_year = ?";

        try {
            var requestExport = dbConnection.prepareStatement(getExport);
            requestExport.setString(1, category);
            requestExport.setInt(2, Integer.parseInt(year));
            var tableExport = requestExport.executeQuery();

            while (tableExport.next())
            {
                var categoryShare = new CountryShare(tableExport.getString(1), tableExport.getDouble(2));

                list.add(categoryShare);
            }
        }
        catch (Exception e) { }

        return list;
    }

    public LinkedList<CountryShare> CategoryShareImport(String category, String year) {
        var list = new LinkedList<CountryShare>();

        var getImport = "SELECT co_name, cai_import FROM CategoryImport " +
                "INNER JOIN Country ON Country.co_codeCountry = CategoryImport.cai_codeCountry " +
                "INNER JOIN Year ON Year.y_codeYear = CategoryImport.cai_codeYear " +
                "INNER JOIN Category ON Category.ca_codeCategory = CategoryImport.cai_codeCategory " +
                "WHERE ca_category = ? AND y_year = ?";

        try {
            var requestImport = dbConnection.prepareStatement(getImport);
            requestImport.setString(1, category);
            requestImport.setInt(2, Integer.parseInt(year));
            var tableImport = requestImport.executeQuery();

            while (tableImport.next())
            {
                var categoryShare = new CountryShare(tableImport.getString(1), tableImport.getDouble(2));

                list.add(categoryShare);
            }
        }
        catch (Exception e) { }

        return list;
    }
}