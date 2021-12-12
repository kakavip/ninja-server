const {query, close} = require("./mysql_async");

const getOptionId = async () => {
  const ob = {};
  const result = await query("SELECT * FROM optionitem");
  for (let option of result) {
    ob[option.id] = option.name;
  }
  return ob;
};

const updateNtgt = (item) => {
  item.buyCoin = parseInt(item.buyCoin / 2);
  const options = item.option;
  if (item.id === 423) {

    options[0].param = 200;
    options[1].param = 100;
    options[2].param = 30;
    options[3].param = 50;
    options[4].param = 10;
    options[5].param = 10;
    options[6].param = 10;
  } else if (item.id === 424) {
    options[0].param = 350;
    options[1].param = 200;
    options[2].param = 70;
    options[3].param = 100;
    options[4].param = 20;
    options[5].param = 20;
    options[6].param = 20;
  } else if (item.id === 425) {
    options[0].param = 700;
    options[1].param = 400;
    options[2].param = 120;
    options[3].param = 150;
    options[4].param = 40;
    options[5].param = 40;
    options[6].param = 40;
  } else if (item.id === 426) {
    options[0].param = 1200;
    options[1].param = 650;
    options[2].param = 220;
    options[3].param = 300;
    options[4].param = 60;
    options[5].param = 60;
    options[6].param = 60;
  } else if (item.id === 427) {
    options[0].param = 2000;
    options[1].param = 1000;
    options[2].param = 400;
    options[3].param = 500;
    options[4].param = 80;
    options[5].param = 80;
    options[6].param = 80;
  }

}

const getNinjas = async () => {
  return await query('select * from ninja');
}

const getItems = async (ninja) => {
  const clone = (await query(`select ItemBody, ItemMounts from clone_ninja where name='${ninja.name}' limit 1`))[0];
  const r = {
    ItemBody: JSON.parse(ninja.ItemBody),
    ItemBag: JSON.parse(ninja.ItemBag),
    ItemBox: JSON.parse(ninja.ItemBox),
    ItemMounts: JSON.parse(ninja.ItemMounts),

  }

  if (clone) {
    console.log(clone);
    r.CloneItemBody = JSON.parse(clone.ItemBody);
    r.CloneItemMounts = JSON.parse(clone.ItemMounts);
  } else {
    r.CloneItemBody = [];
    r.CloneItemMounts = [];
  }

  return r;
}

const updateNinja = async (ninjaName, data) => {
  const sql1 = Object.keys(data).filter(k => k !== 'CloneItemBody' && k !== 'CloneItemMounts').map(key => `${key}='${transform(data[key])}'`).join(',');
  await query(`update ninja set ${sql1} where name='${ninjaName}' limit 1`);
}

const updateClone = async (ninjaName, data) => {
  if (data.ItemBody && data.ItemBody.length === 0) return;
  await query(`update clone_ninja set ItemBody='${transform(data.ItemBody)}' where name='${ninjaName}' limit 1`)
}


const updateShop = async () => {
  try {
    const results = await query("select * from `itemsell`");
    for (let shopData of results) {
      const items = JSON.parse(shopData.ListItem);
      for (let item of items) {
        if (item.id >= 423 && item.id <= 427) {
          updateNtgt(item);
          await query(`update itemsell set ListItem='${transform(items)}' where id=${shopData.id}`);
          const ninjas = await getNinjas();
          for (let ninja of ninjas) {
            const Items = await getItems(ninja);

            for (let i of Items.ItemBody) {
              if (i.id === item.id) {
                i.option = item.option;
              }
            }

            for (let i of Items.ItemBag) {
              if (i.id === item.id) {
                i.option = item.option;
              }
            }

            for (let i of Items.ItemBox) {
              if (i.id === item.id) {
                i.option = item.option;
              }
            }

            for (let i of Items.CloneItemBody) {
              if (i.id === item.id) {
                i.option = item.option;
              }
            }

            await updateNinja(ninja.name, Items);
            await updateClone(ninja.name, {ItemBody: Items.CloneItemBody});


          }
        }

        //         try {
        //             if (item.sys === 1) {
        //                 await query(
        //                     `update item set Option1='${JSON.stringify(
        //         item.option
        //       )}' where id = ${item.id}`
        //                 );
        //             } else if (item.sys === 2) {
        //                 await query(
        //                     `update item set Option2='${JSON.stringify(
        //         item.option
        //       )}' where id = ${item.id}`
        //                 );
        //             } else if (item.sys === 3) {
        //                 await query(
        //                     `update item set Option3='${JSON.stringify(
        //         item.option
        //       )}' where id = ${item.id}`
        //                 );
        //             }
        //         } catch (e) {
        //             console.log(e);
        //         }
      }
    }
  } catch (e) {
    console.log(e);
  }

  console.log("Update shop complete");
};

const tinhLuyen = (paramId, tl) => {
  switch (paramId) {
    case 76: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 50) : 60) : 70) : 90) : 130) : 180) : 250) : 350) : 550;
    }
    case 77: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 40) : 60) : 80) : 100) : 120) : 140) : 200) : 220) : 590;
    }
    case 75:
    case 78: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 25) : 30) : 35) : 40) : 50) : 60) : 80) : 115) : 165;
    }
    case 79: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 1) : 1) : 1) : 5) : 5) : 5) : 5) : 5;
    }
    case 80: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 10) : 15) : 20) : 25) : 30) : 35) : 40) : 45;
    }
    case 84:
    case 86: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 10) : 20) : 30) : 40) : 50) : 100) : 120) : 150) : 200;
    }
    case 85: {
      return 1;
    }
    case 82:
    case 83:
    case 87:
    case 88:
    case 89:
    case 90: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 50) : 60) : 80) : 100) : 125) : 300) : 350) : 400) : 500;
    }
    case 94: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 5) : 10) : 15) : 20) : 25) : 30) : 35) : 40) : 60;
    }
    case 81:
    case 91:
    case 92:
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 5) : 5) : 5) : 10) : 10) : 10) : 10) : 10;
    case 95:
    case 96:
    case 97: {
      return (tl != 8) ? ((tl != 7) ? ((tl != 6) ? ((tl != 5) ? ((tl != 4) ? ((tl != 3) ? ((tl != 2) ? ((tl != 1) ? ((tl != 0) ? 0 : 1) : 5) : 10) : 15) : 20) : 25) : 30) : 40) : 60;
    }
    default: {
      return 0;
    }
  }
}

function updateTL(item) {
  const oldTL = item.option[0].param;
  item.option = [{"param": oldTL, "id": 85}, {"param": 350, "id": 82}, {
    "param": 350,
    "id": 83
  }, {"param": 100, "id": 84}, {"param": 5, "id": 81}, {"param": 25, "id": 80}, {
    "param": 5,
    "id": 79
  }];
  for (let i = 1; i < item.option.length; i++) {
    const option = item.option[i];
    for (let i = 1; i <= oldTL; i++) {
      option.param += tinhLuyen(option.id, i);
    }
  }

}

const updateYoroi2 = async () => {
  const ninjas = await getNinjas()
  for (let ninja of ninjas) {
    try {
      const {ItemBag, ItemBody, ItemBox, CloneItemBody} = await getItems(ninja)
      for (let item of ItemBag) {
        if (item && item.id >= 420 && item.id <= 422) {
          updateTL(item);
        }
      }

      for (let item of ItemBody) {
        if (item && item.id >= 420 && item.id <= 422) {
          updateTL(item);
        }
      }

      for (let item of ItemBox) {
        if (item && item.id >= 420 && item.id <= 422) {
          updateTL(item);
        }
      }

      for (let item of CloneItemBody) {
        if (item && item.id >= 420 && item.id <= 422) {
          updateTL(item);
        }
      }

      await updateNinja(ninja.name, {ItemBag, ItemBody, ItemBox});
      await updateClone(ninja.name, {ItemBody: CloneItemBody});

    } catch (e) {
      console.log(e);
      console.log(n.name);
    }
  }
}

const updateDo9X = async () => {

  // NON 618, 619
  let a1 = [
    {param: 18, id: 47},
    {param: 60, id: 2},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 11},
    {param: 60, id: 17},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: +550, id: 29},
    {param: +180, id: 35},
    {param: +500, id: 50},
  ];
  let a2 = [
    {param: 18, id: 47},
    {param: 60, id: 3},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 12},
    {param: 60, id: 17},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: +550, id: 29},
    {param: +180, id: 33},
    {param: +500, id: 48},
  ];
  let a3 = [
    {param: 18, id: 47},
    {param: 60, id: 4},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 13},
    {param: 60, id: 17},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: +550, id: 29},
    {param: +180, id: 34},
    {param: +500, id: 49},
  ];

  await query(
      `update item set ItemOption='[]', Option1='${JSON.stringify(
          a1
      )}', Option2='${JSON.stringify(a2)}', Option3 = '${JSON.stringify(
          a3
      )}'  where id in (618,619)`
  );
  a1 = [
    {param: 36, id: 47},
    {param: 60, id: 4},
    {param: +150, id: 6},
    {param: +150, id: 7},
    {param: 100, id: 13},
    {param: 100, id: 15},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 33},
    {param: 500, id: 48},
  ];
  a2 = [
    {param: 36, id: 47},
    {param: 60, id: 2},
    {param: +150, id: 6},
    {param: +150, id: 7},
    {param: 60, id: 11},
    {param: 60, id: 15},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 34},
    {param: 500, id: 49},
  ];
  a3 = [
    {param: 34, id: 47},
    {param: 60, id: 3},
    {param: 200, id: 6},
    {param: 200, id: 7},
    {param: 100, id: 12},
    {param: 100, id: 15},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 35},
    {param: 500, id: 50},
  ];
  await query(
      `update item set ItemOption='[]', Option1='${JSON.stringify(
          a1
      )}', Option2='${JSON.stringify(a2)}', Option3 = '${JSON.stringify(
          a3
      )}'  where id in (620,621,622,623)`
  );

  a1 = [
    {param: 18, id: 47},
    {param: 60, id: 3},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 12},
    {param: 60, id: 18},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 34},
    {param: 500, id: 49},
  ];
  a2 = [
    {param: 18, id: 47},
    {param: 60, id: 4},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 13},
    {param: 60, id: 18},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 35},
    {param: 500, id: 50},
  ];
  a3 = [
    {param: 18, id: 47},
    {param: 60, id: 2},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 11},
    {param: 60, id: 18},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 33},
    {param: 500, id: 48},
  ];

  await query(
      `update item set ItemOption='[]', Option1='${JSON.stringify(
          a1
      )}', Option2='${JSON.stringify(a2)}', Option3 = '${JSON.stringify(
          a3
      )}'  where id in (624,625)`
  );
  a1 = [
    {param: 18, id: 47},
    {param: 60, id: 4},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 13},
    {param: 35, id: 16},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 33},
    {param: 500, id: 48},
  ];
  a2 = [
    {param: 18, id: 47},
    {param: 60, id: 2},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 11},
    {param: 35, id: 16},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 34},
    {param: 500, id: 49},
  ];
  a3 = [
    {param: 18, id: 47},
    {param: 60, id: 3},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 12},
    {param: 35, id: 16},
    {param: 16, id: 27},
    {param: 9, id: 28},
    {param: 550, id: 29},
    {param: 180, id: 35},
    {param: 500, id: 50},
  ];

  await query(
      `update item set ItemOption='[]', Option1='${JSON.stringify(
          a1
      )}', Option2='${JSON.stringify(a2)}', Option3 = '${JSON.stringify(
          a3
      )}'  where id in (626,627)`
  );
  a1 = [
    {param: 18, id: 47},
    {param: 60, id: 5},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 12},
    {param: 60, id: 20},
    {param: 16, id: 30},
    {param: 9, id: 31},
    {param: 550, id: 32},
    {param: 120, id: 36},
    {param: 55, id: 46},
  ];
  a2 = [
    {param: 18, id: 47},
    {param: 60, id: 5},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 13},
    {param: 60, id: 20},
    {param: 16, id: 30},
    {param: 9, id: 31},
    {param: 550, id: 32},
    {param: 120, id: 36},
    {param: 55, id: 46},
  ];
  a3 = [
    {param: 18, id: 47},
    {param: 60, id: 5},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 11},
    {param: 60, id: 20},
    {param: 16, id: 30},
    {param: 9, id: 31},
    {param: 550, id: 32},
    {param: 120, id: 36},
    {param: 55, id: 46},
  ];

  await query(
      `update item set ItemOption='[]', Option1='${JSON.stringify(
          a1
      )}', Option2='${JSON.stringify(a2)}', Option3 = '${JSON.stringify(
          a3
      )}'  where id in (628)`
  );

  a1 = [
    {param: 18, id: 47},
    {param: 60, id: 5},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 13},
    {param: 60, id: 14},
    {param: 16, id: 30},
    {param: 9, id: 31},
    {param: 550, id: 32},
    {param: 180, id: 33},
    {param: 800, id: 51},
  ];
  a2 = [
    {param: 18, id: 47},
    {param: 60, id: 5},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 11},
    {param: 60, id: 14},
    {param: 16, id: 30},
    {param: 9, id: 31},
    {param: 550, id: 32},
    {param: 180, id: 34},
    {param: 800, id: 52},
  ];
  a3 = [
    {param: 18, id: 47},
    {param: 60, id: 5},
    {param: 150, id: 6},
    {param: 150, id: 7},
    {param: 60, id: 12},
    {param: 60, id: 14},
    {param: 16, id: 30},
    {param: 9, id: 31},
    {param: 550, id: 32},
    {param: 180, id: 35},
    {param: 800, id: 53},
  ];
  await query(
      `update item set ItemOption='[]', Option1='${JSON.stringify(
          a1
      )}', Option2='${JSON.stringify(a2)}', Option3 = '${JSON.stringify(
          a3
      )}'  where id in (629,630,631)`
  );


  console.log("Update do 9x success");
};

const updateDoNinja = async () => {
  const ninjas = await query("select * from ninja");

  for (let ninja of ninjas) {
    let itemBag = JSON.parse(ninja.ItemBag);
    let itemBody = JSON.parse(ninja.ItemBody);
    let itemBox = JSON.parse(ninja.ItemBox);

    for (let i = 0; i < itemBody.length; i++) {
      const temp = {...itemBody[i]};
      itemBody[i] = await updateItem(temp);
      upgrade(itemBody[i])
    }

    for (let i = 0; i < itemBag.length; i++) {
      const temp = {...itemBag[i]};
      itemBag[i] = await updateItem(temp)
      upgrade(itemBag[i]);
    }

    for (let i = 0; i < itemBox.length; i++) {
      const temp = {...itemBox[i]};
      itemBox[i] = await updateItem(temp)
      upgrade(itemBox[i]);
    }


    await query(
        `update ninja set ItemBag = '${transform(itemBag)}', ItemBody='${transform(itemBody)}',ItemBox='${transform(itemBox)}' where id = ${ninja.id} limit 1`
    );
  }

  const clones = await query("select * from clone_ninja");
  for (let clone of clones) {
    const itemBody = JSON.parse(clone.ItemBody);
    for (let i = 0; i < itemBody.length; i++) {
      const temp = {...itemBody[i]};
      itemBody[i] = await updateItem(temp);
      upgrade(itemBody[i])
    }

    await query(
        `update clone_ninja set ItemBody='${transform(itemBody)}' where id=${
            clone.id
        } limit 1`
    );
  }
};

const transform = (data) => {
  return JSON.stringify(data);
};

function updateYY(item, differ) {
  for (let i = 1; i < differ.length; i++) {
    item.option[i].param -= differ[i];
  }
}

function updateVK(item, options) {
  for (let i = 0; i < options.length; i++) {
    item.option[i] = options[i];
  }
}


const updateYoroi = async () => {
  const yy = await query(`select * from item where type = 12`);
  let i = 0

  for (let y of yy) {
    i++;
    console.log("Update yoroi " + i);
    console.log("Update name " + y.name);
    try {

      const options = JSON.parse(y.ItemOption);
      const differ = [
        0,
        4650,
        4650,
        400,
        45,
        135,
        5
      ];

      options[1].param = 350;

      options[2].param = 350;

      options[3].param = 100;

      options[4].param = 5;

      options[5].param = 25;

      options[6].param = 5;

      await query(`update item set ItemOption='${transform(options)}' where id=${y.id}`);
      const ninjas = await query('select * from ninja');
      for (let ninja of ninjas) {
        const clone = (await query(`select * from clone_ninja where name='${ninja.name}'  limit 1`))[0];

        let ItemBodyClone = [];

        if (clone) {
          ItemBodyClone = JSON.parse(clone.ItemBody);
        }

        const ItemBody = JSON.parse(ninja.ItemBody);
        const ItemBag = JSON.parse(ninja.ItemBag);
        const ItemBox = JSON.parse(ninja.ItemBox);
        for (let i of ItemBodyClone) {
          if (i.id === y.id) {
            updateYY(i, differ);
            break;
          }
        }

        try {
          for (let i of ItemBody) {
            if (i.id === y.id) {
              updateYY(i, differ);
              break;
            }
          }
        } catch (e) {
          console.log(e)
          console.log(ninja.name);
        }

        for (let i of ItemBag) {
          if (i.id === y.id) {
            updateYY(i, differ);
            break;
          }
        }
        for (let i of ItemBox) {
          if (i.id === y.id) {
            updateYY(i, differ);
            break;
          }
        }

        await query(`update ninja set ItemBag='${transform(ItemBag)}', ItemBody='${transform(ItemBody)}', ItemBox='${transform(ItemBox)}' where id=${ninja.id} limit 1`)
        if (clone) {
          await query(`update clone_ninja set ItemBody = '${transform(ItemBodyClone)}' where name = '${ninja.name}' limit 1`);
        }
      }
    } catch (ex) {
      console.log(ex)
    }
    console.log("Update yoroi  success")

  }
}

const upgrade = (item) => {
  const next = item.upgrade;
  for (let i = 0; i < item.option.length; ++i) {
    let option = item.option[i];
    if (option.id === 6 || option.id === 7) {
      option.param += 15 * next;
    } else if (option.id === 8 ||
        option.id === 9 ||
        option.id === 19) {
      option.param += 10 * next;
    } else if (option.id === 10 ||
        option.id === 11 ||
        option.id === 12 ||
        option.id === 13 ||
        option.id === 14 ||
        option.id === 15 ||
        option.id === 17 ||
        option.id === 18 ||
        option.id === 20) {
      option.param += 5 * next;
    } else if (option.id === 21 ||
        option.id === 22 ||
        option.id === 23 ||
        option.id === 24 ||
        option.id === 25 ||
        option.id === 26) {
      option.param += 150 * next;
    } else if (option.id === 16) {
      option.param += 3 * next;
    }


  }
}

const updateItem = async (item) => {
  const sourceItem = (
      await query(`select * from item where id = ${item.id}`)
  )[0];
  let options;
  if (item.sys === 0) return item;
  if (item.sys === 1) {
    options = sourceItem.Option1;
  } else if (item.sys === 2) {
    options = sourceItem.Option2;
  } else if (item.sys === 3) {
    options = sourceItem.Option3;
  }
  item.sale = 5;
  if (!options || options === "[]") return item;

  options = JSON.parse(options);
  const oldOptions = [...item.option];
  if (options) {
    for (let i = 0; i < oldOptions.length; i++) {
      if (i < options.length) {
        oldOptions[i] = options[i];
      }
    }

    if (sourceItem.type === 5) {
      const doTL = findOption(85, oldOptions);
      const chimang = findOption(92, oldOptions);
      if (doTL && chimang) {
        if (doTL.param === 0) {
          oldOptions[chimang.index].param = 10;
        } else if (doTL.param === 1) {
          oldOptions[chimang.index].param = 11;
        } else if (doTL.param === 2) {
          oldOptions[chimang.index].param = 16;
        } else if (doTL.param === 3) {
          oldOptions[chimang.index].param = 21;
        } else if (doTL.param === 4) {
          oldOptions[chimang.index].param = 26;
        } else if (doTL.param === 5) {
          oldOptions[chimang.index].param = 36;
        } else if (doTL.param === 6) {
          oldOptions[chimang.index].param = 46;
        } else if (doTL.param === 7) {
          oldOptions[chimang.index].param = 56;
        } else if (doTL.param === 8) {
          oldOptions[chimang.index].param = 66;
        } else if (doTL.param === 9) {
          oldOptions[chimang.index].param = 76;
        }
      }
    }
  }
  return {
    ...item,
    option: oldOptions,
  };
};

const findOption = (id, options) => {
  let index = 0;
  for (let option of options) {
    if (option && option.id === id)
      return {
        ...option,
        index,
      };
    index++;
  }
};


const updateTBXS = async () => {
  const ID_HP = 77;
  const ID_TC = 76;
  const ID_CX = 75;
  const ID_NE = 78;

  // OK
  await updateItemType(75, 500, [441, 488], 77);
  await updateItemType(76, 500, [440, 487], 76);
  await updateItemType(77, 50, [439], 78);
  await updateItemType(78, 50, [489], 78);

  await updateItemType(78, 50, [442], 75);
  await updateItemType(77, 50, [486], 75);
};


const updateItemType = async (optionId, newParam, itemIds, newId) => {
  const sql = `select id,ItemOption from item where id in (` + itemIds.join(",") + ")";
  console.log("SQL  " + sql)
  const itemType = await query(
      sql
  );

  function updateOption(item, diff, id) {

    for (let i = 0; i < item.option.length; i++) {
      let option = item.option[i];
      if (option.id === id && option.id !== 85) {
        option.param -= diff;
        option.id = newId;
        return {
          i,
          option
        }
      }
    }
  }

  for (let baseItem of itemType) {
    const id = baseItem.id;
    const options = JSON.parse(baseItem.ItemOption);
    let option = findOption(optionId, options);
    if (!option) {
      console.log("NOT FOUND");
      return;
    }
    option = options[option.index];

    const diff = option.param - newParam;
    option.param = newParam;
    option.id = newId;

    await query(
        `update item set ItemOption='${transform(options)}' where id = ${
            baseItem.id
        } limit 1`
    );

    const ninja = await query(
        `select id, ItemMounts, ItemBag, ItemBox,name from ninja`
    );

    const itemShinwas = await query('select * from itemshinwa');
    for (let itemShinwa of itemShinwas) {

      const itemSw = JSON.parse(itemShinwa.item);
      const itemId = itemShinwa.id;

      const item = itemSw.item;

      if (item.id !== id) continue;
      const r = updateOption(item, diff, optionId);
      if (r && r.option) {
        item.option[r.i] = r.option;
      }
      await query(`update itemshinwa set item='${transform({
        id: itemId,
        item: itemSw
      })}' where id = ${itemShinwa.id} limit 1`);
    }


    for (let n of ninja) {
      try {

        const clone = (
            await query(
                `select * from clone_ninja where name= '${n.name}' limit 1`
            )
        )[0];

        const ItemMounts = JSON.parse(n.ItemMounts);
        const ItemBag = JSON.parse(n.ItemBag);
        const ItemBox = JSON.parse(n.ItemBox);

        let ItemMountsClone = [];
        if (clone) {
          ItemMountsClone = JSON.parse(clone.ItemMounts);
        }


        for (let i = 0; i < ItemMounts.length; i++) {
          let item = ItemMounts[i];
          if (item.id !== id) continue;
          const r = updateOption(item, diff, optionId);
          if (r && r.option) {
            item.option[r.i] = r.option;
          }
        }


        for (let i = 0; i < ItemBag.length; i++) {
          let it = ItemBag[i];
          if (it.id !== id) continue;
          const r = updateOption(it, diff, optionId);
          if (r && r.option) {
            it.option[r.i] = r.option;
          }
        }

        for (let i = 0; i < ItemBox.length; i++) {
          let it = ItemBox[i];
          if (it.id !== id) continue;
          const r = updateOption(it, diff, optionId);
          if (r && r.option) {
            it.option[r.i] = r.option;
          }
        }

        for (let i = 0; i < ItemMountsClone.length; i++) {
          let it = ItemMountsClone[i];
          if (it.id !== id) continue;
          const r = updateOption(it, diff, optionId);
          if (r && r.option) {
            it.option[r.i] = r.option;
          }
        }

        await query(
            `update ninja set ItemBox = '${transform(ItemBox)}',
                            ItemBag='${transform(ItemBag)}', ItemMounts='${transform(ItemMounts)}' where id=${n.id} limit 1`
        );

        if (clone) {
          await query(`update clone_ninja set ItemMounts='${transform(ItemMountsClone)}' where name = '${n.name}' limit 1`);
        }

      } catch (e) {
        console.log(e);
        console.log(JSON.stringify(n.name));

      }
    }
  }
};

const updateVK9x = async () => {
  const vk9xs = await query('select * from item where type = 1 and level=90');
  for (let vk of vk9xs) {
    const options = JSON.parse(vk.ItemOption);
    options[0].param = 500;
    options[1].param = 500;
    options[2].param = 150;
    options[3].param = 60;
    options[4].param = 1800;
    options[5].param = 150;
    options[6].param = 16;
    options[7].param = 90;
    options[8].param = 900;
    options[9].param = 140;
    options[10].param = 40
    await query(`update item set ItemOption='${transform(options)}' where id = ${vk.id} limit 1`);

    const ninjas = await query(`select * from ninja`);
    for (let ninja of ninjas) {
      const clone = (await query(`select * from clone_ninja where name='${ninja.name}' limit 1`))[0];
      let ItemBodyClone = [];
      if (clone) {
        ItemBodyClone = JSON.parse(clone.ItemBody);
      }
      const ItemBody = JSON.parse(ninja.ItemBody);
      const ItemBag = JSON.parse(ninja.ItemBag);
      const ItemBox = JSON.parse(ninja.ItemBox);

      for (let item of ItemBodyClone) {


        if (item && item.id === vk.id) {
          updateVK(item, options);

        }
      }

      for (let item of ItemBody) {
        if (item && item.id === vk.id) {
          updateVK(item, options);

        }
      }

      for (let item of ItemBag) {
        if (item && item.id === vk.id) {
          updateVK(item, options);

        }
      }
      for (let item of ItemBox) {
        if (item && item.id === vk.id) {
          updateVK(item, options);
        }
      }

      await query(`update ninja set ItemBag='${transform(ItemBag)}', ItemBody='${transform(ItemBody)}', ItemBox='${transform(ItemBox)}' where id=${ninja.id}`);
      if (clone) {
        await query(`update clone_ninja set ItemBody='${transform(ItemBodyClone)}' where name='${ninja.name}'`);
      }
    }


    const itemShinwas = await query('select * from itemshinwa');
    for (let itemShinwa of itemShinwas) {

      const itemSw = JSON.parse(itemShinwa.item);
      const itemId = itemShinwa.id;

      const item = itemSw.item;

      if (item.id !== vk.id) continue;
      updateVK(item, options);

      await query(`update itemshinwa set item='${transform({
        id: itemId,
        item: itemSw
      })}' where id = ${itemShinwa.id} limit 1`);
    }


  }
}


(async function () {
  try {
    // await updateYoroi();
    // await updateVK9x()
    // await updateDo9X();
    // await updateDoNinja();
    // await updateTBXS();
    await updateShop();
    await updateYoroi2();

  } catch (e) {
    console.log(e);
  }

  close();
})();
