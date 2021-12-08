package ServerController;

import DataBaseHandlers.DBHandler;
import ConnectionManager.ConnectionManager;
import com.example.alltrade.model.country.CountryAdd;
import com.example.alltrade.model.country.CountryImportExport;
import com.example.alltrade.model.user.CurrentUser;
import com.example.alltrade.model.user.User;
import com.example.alltrade.model.user.UserInfo;

import java.io.IOException;

public class ServerController {
    private ConnectionManager manager;
    private DBHandler handler;

    public ServerController(ConnectionManager manager) {
        this.handler = new DBHandler();
        this.manager = manager;
    }

    public void work() throws IOException {
        String massage;

        do {
            switch (massage = manager.readString()) {
                case "authorize":
                    manager.sendObject(Operations.GetAuthorization((User)manager.readObject(), this.handler));
                    break;

                case "regist":
                    manager.sendObject(Operations.GetRegistration((User)manager.readObject(), this.handler));
                    break;

                case "countries":
                    manager.sendObject(Operations.GetListOfCountry(this.handler));
                    break;

                case "years":
                    manager.sendObject(Operations.GetListOfYears(this.handler));
                    break;

                case "categories":
                    manager.sendObject(Operations.GetListOfCategory(this.handler));
                    break;

                case "countryTable":
                    manager.sendObject(Operations.GetCountryImportExport(manager.readString(), this.handler));
                    break;

                case "addCountry":
                    Operations.AddCommonImportExport((CountryAdd)manager.readObject(), this.handler);
                    break;

                case "editCountry":
                    Operations.UpdateCommonImportExport((CountryAdd)manager.readObject(), this.handler );
                    break;

                case "deleteCountry":
                    Operations.DeleteCommonImportExport((CountryAdd)manager.readObject(), this.handler );
                    break;

                case "countryTable2":
                    manager.sendObject(Operations.GetCountryConstituentDB(manager.readString(), this.handler));
                    break;

                case "share":
                    manager.sendObject(Operations.GetCountryShare(manager.readString(), manager.readString(), this.handler));
                    break;

                case "AllUsers":
                    manager.sendObject(Operations.GetAllUsers(this.handler));
                    break;

                case "deleteUser":
                    Operations.DeleteUsers(manager.readString(), this.handler);
                    break;

                case "addUser":
                    Operations.AddUsers((UserInfo)manager.readObject(), this.handler);
                    break;

                case "updateUser":
                    Operations.UpdateUsers((UserInfo)manager.readObject(), this.handler);
                    break;

                case "EIConstituent":
                    manager.sendObject(Operations.GetConstituent(manager.readString(), this.handler));
                    break;

                case "worldCE":
                    manager.sendObject(Operations.GetWorldConstituentExport(manager.readString(), this.handler));
                    break;

                case "Share import":
                    manager.sendObject(Operations.GetCategoryShareImport(manager.readString(), manager.readString(), this.handler));
                    break;

                case "Share export":
                    manager.sendObject(Operations.GetCategoryShareExport(manager.readString(), manager.readString(), this.handler));
                    break;

                case "error":
                    break;

                case "close":
                    manager.Close();
                    massage = null;
                    break;
            }
        } while(massage != null);
    }
}
