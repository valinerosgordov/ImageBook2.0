package ru.minogin.auth.shared.model.user;

import ru.minogin.auth.shared.model.base.BaseUser;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/** A simple user model suitable for most situations. Username is unique.
 * 
 * @author Andrey Minogin */
@Entity
@Table(name = " user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class User extends BaseUser {
}
