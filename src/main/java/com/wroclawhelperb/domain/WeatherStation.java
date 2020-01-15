package com.wroclawhelperb.domain;

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
    public static final String LOTNICZA = "UL. LOTNICZA / UL. KOSMONAUTĂ“W";
    public static final String DMOWSKIEGO = "MOST ROMANA DMOWSKIEGO";
    public static final String SOBIESKIEGO = "AL. JANA III SOBIESKIEGO";
    public static final String OPOLSKA = "UL. OPOLSKA / UL. KATOWICKA";
    public static final String GADOWIANKA = "ESTAKADA GADOWIANKA";
    public static final String MILENIJNY = "MOST MILENIJNY";
    public static final String WARSZAWSKI = "MOST WARSZAWSKI";

    @Id
    @GeneratedValue
    @Column(name = "Id", updatable = false, unique = true)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private GPSLocation location;

    public WeatherStation(String name, GPSLocation location) {
        this.name = name;
        this.location = location;
    }
}
