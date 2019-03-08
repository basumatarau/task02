package by.htp.basumatarau.sql.dao.util;

import by.htp.basumatarau.sql.dao.beans.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomGenerators {
    private static final String MOCKDATA_COUNTRIES_CITIES = "mockData/countries-and-cities.json";
    private static final String MOCKDATA_COMPANIES = "mockData/companies.json";
    private static final String MOCKDATA_EMPLOYEES = "mockData/employees.json";
    private static final String MOCKDATA_JOB_POSITIONS = "mockData/positions.json";
    private static final Random rand = new Random();

    public static class CountryGen implements Generator<Country> {
        private static final List<String> COUNTRIES = new ArrayList<>();

        {
            try (Reader reader = new FileReader(MOCKDATA_COUNTRIES_CITIES)) {
                Map map = new Gson().fromJson(reader, Map.class);
                for (Object o : map.entrySet()) {
                    Map.Entry<String, ArrayList<String>> entry
                            = (Map.Entry<String, ArrayList<String>>) o;
                    COUNTRIES.add(entry.getKey());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Country next() {
            String countryName = COUNTRIES.remove(Math.abs(rand.nextInt() % COUNTRIES.size()));
            Country country = new Country();
            country.setCountry(countryName);
            return country;
        }
    }

    public static class CityGen implements Generator<City> {
        private Country relatedCountry;
        private static final List<String> CITIES = new ArrayList<>();

        public CityGen(Country relatedCountry) {
            this.relatedCountry = relatedCountry;

            try (Reader reader = new FileReader(MOCKDATA_COUNTRIES_CITIES)) {
                Map map = new Gson().fromJson(reader, Map.class);
                for (Object o : map.entrySet()) {
                    Map.Entry<String, ArrayList<String>> entry
                            = (Map.Entry<String, ArrayList<String>>) o;
                    if (entry.getKey().equals(relatedCountry.getCountry())) {
                        CITIES.addAll(entry.getValue());
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int getCapacity(){
            return CITIES.size();
        }

        @Override
        public City next() {
            String cityName = CITIES.remove(Math.abs(rand.nextInt() % CITIES.size()));
            City city = new City();
            city.setCity(cityName);
            return city;
        }
    }

    public static class AddressGen implements Generator<Address> {
        private City relatedCity;
        private int counter = 0;

        public AddressGen(City relatedCity) {
            this.relatedCity = relatedCity;
        }

        @Override
        public Address next() {
            Address address = new Address();
            address.setAddress("adr. stub#" + ++counter + " (in " + relatedCity.getCity() + ")");
            return address;
        }
    }

    public static class CompanyGen implements Generator<Company> {
        private static final List<String> COMPANY_NAMES = new ArrayList<>();

        {
            try (Reader reader = new FileReader(MOCKDATA_COMPANIES)) {
                JsonArray jsonElements = new Gson().fromJson(reader, JsonArray.class);
                for (JsonElement jsonElement : jsonElements) {
                    JsonElement companyName = jsonElement.getAsJsonObject().get("company");
                    COMPANY_NAMES.add(companyName.getAsString());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Company next() {
            Company company = new Company();
            company.setName(COMPANY_NAMES.remove(Math.abs(rand.nextInt() % COMPANY_NAMES.size())));
            return company;
        }
    }

    public static class EmployeeGen implements Generator<Employee> {
        private static final List<String> EMPLOYEE_FIRST_NAMES = new ArrayList<>();
        private static final List<String> EMPLOYEE_LAST_NAMES = new ArrayList<>();

        {
            try (Reader reader = new FileReader(MOCKDATA_EMPLOYEES)) {
                JsonArray jsonElements = new Gson().fromJson(reader, JsonArray.class);
                for (JsonElement jsonElement : jsonElements) {
                    JsonElement firstName = jsonElement.getAsJsonObject().get("first_name");
                    JsonElement lastName = jsonElement.getAsJsonObject().get("last_name");
                    EMPLOYEE_FIRST_NAMES.add(firstName.getAsString());
                    EMPLOYEE_LAST_NAMES.add(lastName.getAsString());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Employee next() {
            Employee employee = new Employee();
            employee.setFirstName(
                    EMPLOYEE_FIRST_NAMES.get(
                            Math.abs(
                                    rand.nextInt() % EMPLOYEE_FIRST_NAMES.size()
                            )
                    )
            );
            employee.setLastName(
                    EMPLOYEE_LAST_NAMES.get(
                            Math.abs(
                                    rand.nextInt() % EMPLOYEE_LAST_NAMES.size()
                            )
                    )
            );
            return employee;
        }
    }
    public static class PositionGen implements Generator<Position>{
        private static final List<String> EMPLOYEE_POSITIONS = new ArrayList<>();

        {
            try (Reader reader = new FileReader(MOCKDATA_JOB_POSITIONS)) {
                JsonArray jsonElements = new Gson().fromJson(reader, JsonArray.class);
                for (JsonElement jsonElement : jsonElements) {
                    JsonElement position = jsonElement.getAsJsonObject().get("job_position");
                    EMPLOYEE_POSITIONS.add(position.getAsString());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        @Override
        public Position next() {
            Position position = new Position();
            position.setName(
                    EMPLOYEE_POSITIONS.get(
                            Math.abs(
                                    rand.nextInt() % EMPLOYEE_POSITIONS.size()
                            )
                    )
            );
            return position;
        }
    }
}
