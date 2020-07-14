//mongo localhost/Peachkite phase2_deployment_script.js
//mongorestore --db Peachkite dump/Peachkite/
//mngodump --db Peachkite
conn = new Mongo();
db = conn.getDB("Peachkite");
print("hi");
var categoryRatings={1:0.0,2:0.0,3:0.0,4:0.0};
print("hi");
var benefitIdMap={};
print("hi");
var cnt=1;
db.getCollection("Benefit").find().forEach(function(benefit){
    benefit.ctgry=NumberLong(cnt++);
    benefitIdMap[benefit._id.valueOf()]=benefit.ctgry;
    if(cnt == 5)
        cnt=1;
    print(benefit.ctgry);
    db.getCollection("Benefit").save(benefit);
});
db.getCollection("Company").find().forEach(function(company){
    print(company.nm+" "+company._id.valueOf());
    company.catrts=categoryRatings;
    for(var i in company.bfts){
        var benefit=company.bfts[i];
        benefit.ctgry=benefitIdMap[benefit._id.valueOf()];
        benefit.isslctd=true;
        benefit.typ=1;
        benefit.rt=0.0;
    }
    db.getCollection("Company").save(company);
});
