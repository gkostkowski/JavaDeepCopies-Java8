package com.pwr.ztp.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by Grzesiek on 2017-04-10.
 */
public class Country implements Cloneable, Serializable {
    private String name;
    private boolean isInEU;
    private boolean isInNATO;
    private double grossDomesticProd; // in mln USD
    private String president;
    private List<Province> provinces;

    public Country() {
    }


    public Country(String name, boolean isInEU, boolean isInNATO, double grossDomesticProd, String president,
                   List<Province> provinces) {
        this.name = new String(name);
        this.isInEU = isInEU;
        this.isInNATO = isInNATO;
        this.grossDomesticProd = grossDomesticProd;
        this.president = new String(president);
        this.provinces = provinces;
    }

    public long getTotalPopulation() {
        return provinces.stream().mapToLong(Province::getTotalPopulation).reduce(0, (p1, p2) -> p1+p2);
    }

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     *
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     *
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by {@code super.clone} before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by {@code super.clone}
     * need to be modified.
     *
     * The method {@code clone} for class {@code Object} performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface {@code Cloneable}, then a
     * {@code CloneNotSupportedException} is thrown. Note that all arrays
     * are considered to implement the interface {@code Cloneable} and that
     * the return type of the {@code clone} method of an array type {@code T[]}
     * is {@code T[]} where T is any reference or primitive type.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     *
     * The class {@code Object} does not itself implement the interface
     * {@code Cloneable}, so calling the {@code clone} method on an object
     * whose class is {@code Object} will result in throwing an
     * exception at run time.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        List<Province> newProvinces = new ArrayList<>();
        for (Province p : provinces)
            newProvinces.add((Province) p.clone());
        return new Country(name, isInEU, isInNATO, grossDomesticProd, president, newProvinces);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", isInEU=" + isInEU +
                ", isInNATO=" + isInNATO +
                ", grossDomesticProd=" + grossDomesticProd +
                ", president='" + president + '\'' +
                ", provinces=" + provinces +
                '}';
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public String getPresident() {
        return president;
    }

    public Province getProvinceByName(String name) {
        return provinces.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().get();

    }

    public String getName() {
        return name;
    }

    public static Country getCountryByName(List<Country> countries, String name) {
        return countries.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().get();
    }

    public List<City> getCities() {
        List<City> res = new ArrayList<>();
        for (Province p: provinces)
            res.addAll(p.getCities());
        return res;
    }

    //java 8 methods

    public List<City> getBigCities(int minPopulation) {
        return getCities().stream().filter(c -> c.getPopulation() >= minPopulation).distinct().collect(toList());
    }

    public double getBigCitiesImpact(int minPopulation) {
        return getBigCities(minPopulation).stream().mapToInt(City::getPopulation).reduce(0, (c1, c2) -> c1+ c2) / (double)getTotalPopulation();
    }

    public Map<String, List<City>> getBigCitiesProvinces() {
        return provinces.stream().collect(Collectors.toMap(Province::getName,
                p -> p.cities.stream().filter(c -> c.getArea() > 150.0).collect(Collectors.toList())));
    }

    public boolean isInEU() {
        return isInEU;

    }

    public double getGrossDomesticProd() {
        return grossDomesticProd;
    }


}
