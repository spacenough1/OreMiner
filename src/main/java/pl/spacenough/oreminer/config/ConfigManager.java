package pl.astrocode.logowanie.config;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import pl.astrocode.logowanie.Logowanie;

public class ConfigManager {
  private final Logowanie plugin;
  private FileConfiguration config;

  public ConfigManager(@NotNull Logowanie plugin) {
    this.plugin = plugin;
    plugin.saveDefaultConfig();
    this.config = plugin.getConfig();
  }

  private String invalidUsage;
  private String playerAlreadyRegistered;
  private String loginAttemptWithoutRegistration;
  private String passwordsAreNotIdentical;
  private String passwordToWeak;
  private String wrongAnswer;
  private String cannotConnectToServer;
  private String registeredSuccessful;
  private String loginSuccessful;
  private String providedPasswordIsIncorrect;
  private String postServer;
  private int minPassLength;

  public void loadConfig() {
    config = plugin.getConfig();
    invalidUsage = config.getString("messages.invalidUsage");
    playerAlreadyRegistered = config.getString("messages.playerAlreadyRegistered");
    loginAttemptWithoutRegistration = config.getString("messages.loginAttemptWithoutRegistration");
    passwordsAreNotIdentical = config.getString("messages.passwordsAreNotIdentical");
    passwordToWeak = config.getString("messages.passwordToWeak");
    wrongAnswer = config.getString("messages.wrongAnswer");
    cannotConnectToServer = config.getString("messages.cannotConnectToServer");
    registeredSuccessful = config.getString("messages.registeredSuccessful");
    loginSuccessful = config.getString("messages.loginSuccessful");
    providedPasswordIsIncorrect = config.getString("messages.providedPasswordIsIncorrect");
    postServer = config.getString("post-login-server");
    minPassLength = config.getInt("min-pass-length");
  }

  public String getPostServer() {
    return postServer;
  }

  public int getMinPassLength() {
    return minPassLength;
  }

  public String getInvalidUsage() {
    return invalidUsage;
  }

  public String getPlayerAlreadyRegistered() {
    return playerAlreadyRegistered;
  }

  public String getLoginAttemptWithoutRegistration() {
    return loginAttemptWithoutRegistration;
  }

  public String getPasswordsAreNotIdentical() {
    return passwordsAreNotIdentical;
  }

  public String getPasswordToWeak() {
    return passwordToWeak;
  }

  public String getWrongAnswer() {
    return wrongAnswer;
  }

  public String getCannotConnectToServer() {
    return cannotConnectToServer;
  }

  public String getRegisteredSuccessful() {
    return registeredSuccessful;
  }

  public String getLoginSuccessful() {
    return loginSuccessful;
  }

  public String getProvidedPasswordIsIncorrect() {
    return providedPasswordIsIncorrect;
  }
}
