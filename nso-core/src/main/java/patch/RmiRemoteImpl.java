package patch;

import com.lib.rmi.RmiRemote;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import real.*;
import server.GameScr;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static real.ItemData.EXP_ID;
import static real.ItemData.itemDefault;

public class RmiRemoteImpl extends UnicastRemoteObject implements RmiRemote, Serializable {
    public RmiRemoteImpl() throws RemoteException {
    }

    @Override
    public void addXuMessage(String ninjaName, int xu) throws RemoteException {
        // PlayerManager.getInstance().getPlayer(ninjaName).lockAcc();
        // ninja.upxuMessage(xu);
        // ninja.flush();
    }

    @Override
    public void addLuongMessage(String playerName, int luong) throws RemoteException {
        // PlayerManager.getInstance().getPlayer(playerName).lockAcc();
        // player.upluongMessage(luong);
        // player.flush();
    }

    @Override
    public void buyItemById(String ninjaName, int id, int quantity, int upgrade) throws RemoteException {
        // Item item = ItemData.itemDefault(id);
        // if (upgrade != 0 && ItemData.isTypeBody(id)) {
        // item.upgradeNext((byte) upgrade);
        // } else if (id >= 652 && id <= 655 && GameScr.exps.containsKey(upgrade)) {
        // for (int i = 0; i < quantity; i++) {
        // val itemClone = item.clone();
        // final int index = itemClone.option.indexOf(new Option(EXP_ID, 0));
        // if (index != -1) {
        // itemClone.option.get(index).param = GameScr.exps.get(upgrade);
        // }
        // GameScr.upgradeNgoc(itemClone, 1, upgrade);
        // itemClone.setLock(false);
        // itemClone.setUpgrade((byte) upgrade);
        // PlayerManager.getInstance().getNinja(ninjaName).addItemBag(false, itemClone);
        // }

        // return;
        // } else {
        // item.quantity = quantity;
        // }
        // PlayerManager.getInstance().getNinja(ninjaName).p.lockAcc();

    }

    @SneakyThrows
    @Override
    public void chuyenSinh(String ninjaName) throws RemoteException {
        // PlayerManager.getInstance().getPlayer(ninjaName).lockAcc();
        // p.updateExp(Level.getMaxExp(71) - 1L, false);
        // p.nj.setLevel(70);
        // p.upluongMessage(5_000_000L);
        // p.nj.upxuMessage(300_000_000L);
        // p.nj.upyenMessage(600_000_000L);
        // p.nj.ItemBody[1] = itemDefault(194);
        // p.nhanQua = true;
    }

    @SneakyThrows
    @Override
    public void chuyenPhai(@NotNull String ninjaName, @NotNull String phaiName) throws RemoteException {
        // PlayerManager.getInstance().getPlayer(ninjaName).lockAcc();
        // val n = n1.get();

        // final User user = n1.p;

        // user.removeClassItem();
        // if (phaiName.equals("QUAT")) {
        // user.Admission(Constants.QUAT);
        // } else if (phaiName.equals("TIEU")) {
        // user.Admission(Constants.TIEU);
        // } else if (phaiName.equals("KIEM")) {
        // user.Admission(Constants.KIEM);
        // } else if (phaiName.equals("KUNAI")) {
        // user.Admission(Constants.KUNAI);
        // } else if (phaiName.equals("DAO")) {
        // user.Admission(Constants.DAO);
        // } else if (phaiName.equals("CUNG")) {
        // user.Admission(Constants.CUNG);
        // }
    }
}
