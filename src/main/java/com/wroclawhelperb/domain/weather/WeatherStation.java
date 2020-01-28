package com.wroclawhelperb.domain.weather;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "WeatherStation")
@Table(name = "weatherStation")
public class WeatherStation implements Locable {
    public static final String LOTNICZA = "UL. LOTNICZA / UL. KOSMONAUTÓW";
    public static final String DMOWSKIEGO = "MOST ROMANA DMOWSKIEGO";
    public static final String SOBIESKIEGO = "AL. JANA III SOBIESKIEGO";
    public static final String OPOLSKA = "UL. OPOLSKA / UL. KATOWICKA";
    public static final String GADOWIANKA = "ESTAKADA GADOWIANKA";
    public static final String MILENIJNY = "MOST MILENIJNY";
    public static final String WARSZAWSKI = "MOST WARSZAWSKI";

    @Id
    @Column(name = "shortName", nullable = false, updatable = false, unique = true)
    private String shortName;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private GPSLocation location;

    public static String nameToShortName(String name) {
        if (name.equals(LOTNICZA)) {
            return "LOTNICZA";
        }
        if (name.equals(DMOWSKIEGO)) {
            return "DMOWSKIEGO";
        }
        if (name.equals(SOBIESKIEGO)) {
            return "SOBIESKIEGO";
        }
        if (name.equals(OPOLSKA)) {
            return "OPOLSKA";
        }
        if (name.equals(GADOWIANKA)) {
            return "GADOWIANKA";
        }
        if (name.equals(MILENIJNY)) {
            return "MILENIJNY";
        }
        if (name.equals(WARSZAWSKI)) {
            return "WARSZAWSKI";
        }
        return "";
    }

    @Override
    public String toString() {
        return "WeatherStation{" +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
