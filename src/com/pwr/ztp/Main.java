package com.pwr.ztp;

import com.pwr.ztp.cloners.CloneCopier;
import com.pwr.ztp.cloners.ExternalCopier;
import com.pwr.ztp.cloners.SerializationCopier;
import com.pwr.ztp.core.Country;
import com.pwr.ztp.core.Province;
import com.pwr.ztp.gen.CountryGenerator;
import com.pwr.ztp.gen.CustomTerritorities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingDouble;

public class Main {

    public static void main(String[] args) throws Exception {
        //List<Country> countries =performDeepCopies();
        List<Country> countries = new CustomTerritorities().getCountries();
        Country poland = Country.getCountryByName(countries, "Poland");
        //System.out.println(countries);


        int minPopulation=500000;
        System.out.println(poland.getBigCities(minPopulation));
        System.out.println("poland.getBigCitiesImpact(minPopulation): "+poland.getBigCitiesImpact(minPopulation));
        Province dolnoslaskie=null;
        System.out.println(dolnoslaskie=poland.getProvinceByName("Dolnośląskie"));
        System.out.println("poland.getTotalPopulation(): "+poland.getTotalPopulation());
        System.out.println("dolnoslaskie.CountByRegion(): \n" +dolnoslaskie.CountByRegion());
        System.out.println("dolnoslaskie.getBiggestCity(): \n" +dolnoslaskie.getBiggestCity());
        System.out.println("dolnoslaskie.getBiggestCityPopulation(): " +dolnoslaskie.getBiggestCityPopulation());
        System.out.println("dolnoslaskie.getCityPopulation(): \n"+dolnoslaskie.getCityPopulation());
        System.out.println("poland.getBigCitiesProvinces(): \n"+poland.getBigCitiesProvinces());
        System.out.println("getAveragesPKBByEUMembership(countries): \n"+getAveragesPKBByEUMembership(countries));
    }


    private static Map<Boolean, Double> getAveragesPKBByEUMembership(List<Country> countries) {
        return countries.stream().collect(
                Collectors.groupingBy(Country::isInEU, averagingDouble(Country::getGrossDomesticProd)));
    }

    private static List<Country> performDeepCopies() throws Exception {

        CountryGenerator cg2 = new CountryGenerator(3);
        long tstart = System.currentTimeMillis();
        List<Country> res = cg2.callGenerator(1, 20, 5);
        System.out.println("Czas generowania (1x20x5): " + (System.currentTimeMillis() - tstart));

        //SerializationCopier
        List<Country> copy = runSerializationCloner(res);
        System.out.println("[runSerializationCloner] runChecks(res, copy) -> " + runChecks(res, copy));

        copy = runCloner(res);
        System.out.println("[runCloner] runChecks(res, copy) -> " + runChecks(res, copy));

        //ExternalCopier
        copy = runExternalCopier(res);
        System.out.println("[runExternalCopier] runChecks(res, copy) -> " + runChecks(res, copy));

        //test metody sprawdzającej roznorodnosc obiektow
        System.out.println("[test] runChecks(res, res) -> "+runChecks(res, res));
        copy.get(0).getProvinces().set(6, res.get(0).getProvinces().get(6));
        System.out.println("runChecks(res, copy) po zmianie: -> "+runChecks(res, copy));
        return res;
    }

    private static List<Country> runCloner(List<Country> res) throws Exception {
        long tstart = System.currentTimeMillis();
        List<Country> copy = (List<Country>) CloneCopier.deepCopy(res);
        System.out.println("[runCloner] Czas kopiowania (1x20x5): " + (System.currentTimeMillis() - tstart));
        System.out.println("---> test");
        System.out.println(res.get(0).getPresident() == copy.get(0).getPresident());

        return copy;
    }

    private static List<Country> runSerializationCloner(List<Country> res) throws Exception {
        long tstart = System.currentTimeMillis();
        List<Country> copy = (List<Country>) SerializationCopier.deepCopy(res);
        System.out.println("[runSerializationCloner] Czas kopiowania (1x20x5): " + (System.currentTimeMillis() - tstart));
        return copy;
    }

    private static boolean runChecks(List<Country> original, List<Country> copy) {
        boolean res = true;
        for (int i = 0; i < original.size(); i++) {
            res = res && Validator.areDiverse(original.get(i), copy.get(i));
        }
        return res;
    }

    private static List<Country> runExternalCopier(List<Country> source) throws Exception {
        long tstart = System.currentTimeMillis();
        List<Country> copy = (List<Country>) ExternalCopier.deepCopy(source);
        System.out.println("[runExternalCopier] Czas kopiowania (1x20x5): " + (System.currentTimeMillis() - tstart));
        return copy;
    }
}
