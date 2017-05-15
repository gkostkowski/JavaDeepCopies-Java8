package com.pwr.ztp.gen

import com.pwr.ztp.core.City
import com.pwr.ztp.core.Country
import com.pwr.ztp.core.Province

/**
 * Created by Grzesiek on 2017-05-07.
 */
class CustomTerritorities {

    List<Country> countries

    CustomTerritorities(List<Country> countries) {
        this.countries = countries
    }

    CustomTerritorities() {
        this.countries = createCustomCountries()
    }

    List<Country> createCustomCountries() {
        def poland = new Country("Poland", true, true, 474893, "Andrzej Duda", [
                new Province("Dolnośląskie", 40541, "wojewoda",
                        [
                                new City("Wrocław", 690000, "East", 292.82),
                                new City("Bolesławiec", 37000, "South-West", 32.11)
                        ] as List<City>),
                new Province("Śląskie", 45093, "wojewoda", [
                        new City("Katowice", 810000, "South", 270.87),
                ] as List<City>),
                new Province("Podkarpackie", 31902, "wojewoda", [
                        new City('Rzeszów', 559000, "South-East", 249.00),
                ] as List<City>),
                new Province("Łódzkie", 19953, "wojewoda", [
                        new City('Łódź', 780000, "Centre", 210.74),
                ] as List<City>),
                new Province("Świętorzyskie", 16764, "wojewoda", [
                        new City('Kielce', 210900, "South", 109.94),
                ] as List<City>),
        ] as List<Province>)

        poland.getProvinceByName("Dolnośląskie").addNeighbour(poland.getProvinceByName("Śląskie"))
        poland.getProvinceByName("Śląskie").addNeighbour(poland.getProvinceByName("Dolnośląskie"))
        poland.getProvinceByName("Dolnośląskie").addNeighbour(poland.getProvinceByName("Łódzkie"))
        poland.getProvinceByName("Łódzkie").addNeighbour(poland.getProvinceByName("Dolnośląskie"))
        poland.getProvinceByName("Łódzkie").addNeighbour(poland.getProvinceByName("Świętorzyskie"))
        poland.getProvinceByName("Świętorzyskie").addNeighbour(poland.getProvinceByName("Łódzkie"))
        poland.getProvinceByName("Świętorzyskie").addNeighbour(poland.getProvinceByName("Podkarpackie"))
        poland.getProvinceByName("Podkarpackie").addNeighbour(poland.getProvinceByName("Świętorzyskie"))
        return [poland] as List<Country>
    }

    List<Country> getCountries() {
        return countries
    }
}
