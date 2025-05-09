package ru.imagebook.shared.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.minogin.auth.shared.model.base.BaseUser;

@Entity
@Table(name = "xuser")
public class XUser extends BaseUser {
}
