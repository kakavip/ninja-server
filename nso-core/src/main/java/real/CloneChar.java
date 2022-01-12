package real;

import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import server.SQLManager;
import server.Service;
import server.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CloneChar extends Body {

    @NotNull
    public final Ninja chuThan;
    public int percendame;
    private boolean islive;
    public int buff1 = 0;
    public int buff2 = 0;
    public int buff3 = 0;

    public CloneChar(final @NotNull Ninja n) {
        this.percendame = 0;
        this.chuThan = n;
        this.setIslive(false);
        try {
            this.seNinja(n);
            this.id = -n.id - 100000;
            this.ItemBody = new Item[16];
            this.ItemMounts = new Item[5];
            this.KSkill = new byte[3];
            this.OSkill = new byte[5];
            for (byte i = 0; i < this.KSkill.length; ++i) {
                this.KSkill[i] = -1;
            }
            for (byte i = 0; i < this.OSkill.length; ++i) {
                this.OSkill[i] = -1;
            }
            this.isHuman = false;
            this.isNhanban = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static CloneChar getClone(final @NotNull Ninja n) {
        try {
            final CloneChar cl = new CloneChar(n);

            SQLManager.executeQuery("SELECT * FROM `clone_ninja` WHERE `name`LIKE'" + n.name + "';", (red) -> {
                val haveClone = red.first();
                if (haveClone) {
                    cl.id = red.getInt("id");
                    if (red.getShort("level") < 20) {
                        cl.speed = 10;
                        cl.setExp(Level.getMaxExp(21) - 1L);
                        cl.setLevel(20);
                        cl.ItemBody[1] = ItemData.itemDefault(194);
                        final Skill skill2 = new Skill();
                        cl.setSkills(new ArrayList<>());
                        cl.getSkills().add(skill2);
                    } else {
                        cl.speed = red.getByte("speed");
                        cl.nclass = red.getByte("class");

                        try {
                            cl.setKyNangSo(red.getInt("kynangso"));
                            cl.setTiemNangSo(red.getInt("tiemnangso"));
                            cl.setBanghoa(red.getInt("banghoa"));
                            cl.setPhongLoi(red.getInt("phongloi"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        cl.updatePpoint(red.getShort("ppoint"));
                        cl.setPotential0(red.getShort("potential0"));
                        cl.setPotential1(red.getShort("potential1"));
                        cl.setPotential2(red.getInt("potential2"));
                        cl.setPotential3(red.getInt("potential3"));
                        cl.setSpoint(red.getShort("spoint"));
                        JSONArray jar = (JSONArray) JSONValue.parse(red.getString("skill"));
                        if (jar != null) {
                            for (byte b = 0; b < jar.size(); ++b) {
                                final JSONObject job = (JSONObject) jar.get(b);
                                final Skill skill = new Skill();
                                skill.id = Byte.parseByte(job.get("id").toString());
                                skill.point = Byte.parseByte(job.get("point").toString());
                                cl.getSkills().add(skill);
                            }
                        }
                        JSONArray jarr2 = (JSONArray) JSONValue.parse(red.getString("KSkill"));
                        cl.KSkill = new byte[jarr2.size()];
                        for (byte j = 0; j < cl.KSkill.length; ++j) {
                            cl.KSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                        }
                        jarr2 = (JSONArray) JSONValue.parse(red.getString("OSkill"));
                        cl.OSkill = new byte[jarr2.size()];
                        for (byte j = 0; j < cl.OSkill.length; ++j) {
                            cl.OSkill[j] = Byte.parseByte(jarr2.get(j).toString());
                        }
                        cl.setCSkill(Byte.parseByte(red.getString("CSkill")));
                        cl.setLevel(red.getShort("level"));
                        cl.setExp(red.getLong("exp"));
                        cl.expdown = red.getLong("expdown");
                        cl.pk = red.getByte("pk");
                        cl.ItemBody = new Item[16];
                        jar = (JSONArray) JSONValue.parse(red.getString("ItemBody"));
                        if (jar != null) {
                            for (byte j = 0; j < jar.size(); ++j) {
                                final JSONObject job2 = (JSONObject) jar.get(j);
                                final byte index = Byte.parseByte(job2.get("index").toString());
                                cl.ItemBody[index] = ItemData.parseItem(jar.get(j).toString());
                            }
                        }
                        cl.ItemMounts = new Item[5];
                        jar = (JSONArray) JSONValue.parse(red.getString("ItemMounts"));
                        if (jar != null) {
                            for (byte j = 0; j < jar.size(); ++j) {
                                final JSONObject job2 = (JSONObject) jar.get(j);
                                final byte index = Byte.parseByte(job2.get("index").toString());
                                cl.ItemMounts[index] = ItemData.parseItem(jar.get(j).toString());
                            }
                        }
                        jar = (JSONArray) JSONValue.parse(red.getString("effect"));

                        try {
                            if (jar != null) {
                                for (int i = 0; i < jar.size(); i++) {
                                    val effect = Effect.fromJSONObject((JSONObject) jar.get(i));
                                    cl.addEffect(effect);
                                }
                            }
                        } catch (Exception e) {

                        }
                    }

                }
                try {
                    red.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (!haveClone) {
                    SQLManager.executeUpdate(
                            "INSERT INTO clone_ninja(`id`,`name`,`ItemBody`,`ItemMounts`, `ItemBag`, `ItemBox`) VALUES ("
                                    + (-10000000 - n.id) + ",'" + n.name + "','[]','[]', '[]', '[]');");
                    cl.id = -10000000 - n.id;
                    cl.speed = 10;
                    cl.setExp(Level.getMaxExp(21) - 1L);
                    cl.setLevel(20);
                    cl.ItemBody[1] = ItemData.itemDefault(194);
                    final Skill skill2 = new Skill();
                    cl.getSkills().add(skill2);
                }

            });

            return cl;
        } catch (Exception e) {
            return null;
        }
    }

    public void refresh() {
        this.hp = this.getMaxHP();
        this.mp = this.getMaxMP();
        this.x = (short) util.nextInt(this.chuThan.x - 30, this.chuThan.x + 30);
        this.y = this.chuThan.y;
        this.isDie = false;
    }

    public void move(final short x, final short y) {
        this.x = x;
        this.y = y;
        this.chuThan.getPlace().move(this.id, x, y);
    }

    public void off() {
        this.chuThan.timeRemoveClone = -1L;
        this.setIslive(false);
        this.chuThan.getPlace().removeMessage(this.id);
    }

    public void open(final long time, final int percentdame) {
        if (!this.isDie) {
            if (this.chuThan.getPlace() != null) {
                this.chuThan.getPlace().removeMessage(this.id);
            }
        }

        this.chuThan.timeRemoveClone = time;
        this.percendame = percentdame;
        this.setIslive(true);
        this.refresh();
        if (chuThan.getPlace() != null) {
            for (User user : chuThan.getPlace().getUsers()) {
                Service.sendclonechar(this.chuThan.p, user);
            }
        }

    }

    @SneakyThrows
    public void flush() {
        final JSONArray jarr = new JSONArray();
        String sqlSET = "`class`=" + this.nclass + ",`ppoint`="
                + this.getPpoint() + ",`potential0`=" + this.getPotential0() + ",`potential1`="
                + this.getPotential1() + ",`potential2`=" + this.getPotential2() + ",`potential3`="
                + this.getPotential3()
                + ",`spoint`=" + this.getSpoint() + ",`level`=" + this.getLevel() + ",`exp`=" + this.getExp()
                + ",`expdown`=" + this.expdown + ",`pk`=" + this.pk + "";
        jarr.clear();
        for (final Skill skill : this.getSkills()) {
            jarr.add(SkillData.ObjectSkill(skill));
        }
        sqlSET = sqlSET + ",`skill`='" + jarr.toJSONString() + "'";
        jarr.clear();
        for (final byte oid : this.KSkill) {
            jarr.add(oid);
        }
        sqlSET = sqlSET + ",`KSkill`='" + jarr.toJSONString() + "'";
        jarr.clear();
        for (final byte oid : this.OSkill) {
            jarr.add(oid);
        }
        sqlSET = sqlSET + ",`OSkill`='" + jarr.toJSONString() + "',`CSkill`=" + this.getCSkill() + "";
        jarr.clear();
        for (byte j = 0; j < this.ItemBody.length; ++j) {
            final Item item = this.ItemBody[j];
            if (item != null) {
                jarr.add(ItemData.ObjectItem(item, j));
            }
        }
        sqlSET = sqlSET + ",`ItemBody`='" + jarr.toJSONString() + "'";
        jarr.clear();
        for (byte j = 0; j < this.ItemMounts.length; ++j) {
            final Item item = this.ItemMounts[j];
            if (item != null) {
                jarr.add(ItemData.ObjectItem(item, j));
            }
        }
        sqlSET = sqlSET + ",`ItemMounts`='" + jarr.toJSONString() + "'";
        jarr.clear();
        for (Effect effect : this.getVeff()) {
            if (Effect.isPermanentEffect(effect)) {
                jarr.add(effect.toJSONObject());
            }
        }
        sqlSET = sqlSET + ",`effect`='" + jarr.toJSONString() + "'";

        sqlSET = sqlSET + ",`phongloi`=" + this.getPhongLoi() + "";
        sqlSET = sqlSET + ",`banghoa`=" + this.getBanghoa() + "";
        sqlSET = sqlSET + ",`tiemnangso`=" + this.getTiemNangSo() + "";
        sqlSET = sqlSET + ",`kynangso`=" + this.getKyNangSo() + "";

        jarr.clear();
        SQLManager.executeUpdate("UPDATE `clone_ninja` SET " + sqlSET + " WHERE `id`=" + this.id + " LIMIT 1;");
    }

    public boolean isIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public short[] getWinBuffSkills() {
        short[] skills = new short[] { -1, -1, -1 };
        int idx = 0;
        List<Skill> skillArrayList = this.getSkills();
        for (int i = 0, skillArrayListSize = skillArrayList.size(); i < skillArrayListSize; i++) {
            Skill skill1 = skillArrayList.get(i);
            if (skill1.id == 51 || skill1.id == 52 || skill1.id == 47) {
                skills[idx++] = skill1.id;
            }
        }
        return skills;
    }
}
