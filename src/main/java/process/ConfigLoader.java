/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package process;



import java.io.*;
import java.util.Properties;

/**
 *
 * @author BRYAN
 */
public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getResourceAsStream("/application.properties")) {
            if (input == null) throw new FileNotFoundException("Arquivo de configurações não encontrado.");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
