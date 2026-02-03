# 🛡️ 🌐🔒 Spring Security — ROLES Y PRIVILEGIOS️

## 📝 Clase 41 - INTRODUCCION ROLES , PRIVILEGIOS Y AUTENTICACION 🔒 🔒 🔑🔑

![img](../img/img_14.png)

![img](../img/img_17.png)

---

![img](../img/img_15.png)

![img](../img/img_18.png)

---

![img](../img/img_16.png)

![img](../img/img_19.png)
    
---
## 📝 Clase 42 - Agregando authorities a la base de datos  🔒 🔑🔑

- Se crean dos tablas customers - roles
- aplicamos JOIN

![img](../img/img_20.png)

## 📝 Clase 43 - Modificando entidades y autenticando provider 🔒 🔑🔑

- En CustomerEntity se agrega 
```java

@OneToMany(fetch = FetchType.EAGER)
@JoinColumn(name = "id_customer")
private List<RoleEntity> roles;
```

- Se crea clase RoleEntity -> esta es la hija de CustomerEntity

```java
@Entity
@Table(name = "roles")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "role_name")
    private String name;
    private String description;
}

```

- En MyAuthenticationProvider se agrega:

![img](/img/img_21.png)