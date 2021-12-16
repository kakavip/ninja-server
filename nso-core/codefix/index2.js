const itemOptionTemplates = require("./itemOptiontemplate.json");
const itemTemplates = require("./itemtemplates.json");
const { query, close } = require("./mysql_async");
(async function () {
    try {
        for (let i = 127; i < itemOptionTemplates.length; i++) {
            await query(
                `insert into \`optionitem\`(id, name, type) values (${i},'${itemOptionTemplates[i].name}', ${itemOptionTemplates[i].type})`
            );
        }

        for (let i = 791; i < itemTemplates.length; i++) {
            console.log(itemTemplates[i].iconID);
            await query(`insert into \`item\`(id, type, class, skill, gender, name, description, level, iconID, part, uptoup, isExpires,
                  secondsExpires, saleCoinLock, ItemOption, Option1, Option2, Option3) values(${i},${itemTemplates[i].type},0,0,${itemTemplates[i].gender},'${itemTemplates[i].name}',
                  '${itemTemplates[i].discription}',${itemTemplates[i].level},${itemTemplates[i].iconID}, ${itemTemplates[i].part}, ${itemTemplates[i].isUpToUp},0,0,5,'[]','[]','[]','[]')`);
        }
    } catch (e) {
        console.log(e);
    } finally {
        close();
    }
})();
