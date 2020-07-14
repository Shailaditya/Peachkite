export class Util{

  public static findElementPosition(obj:any):number {
    if(obj){
      let curtop:number = 0;
      if (obj.offsetParent) {
        do {
          curtop += obj.offsetTop;
        } while (obj = obj.offsetParent);
        return curtop;
      }
    }else{
      return null;
    }
  }

  public static getHeaderHeight():number {
    return document.getElementById("navbar-container-div")?
      document.getElementById("navbar-container-div").offsetHeight:null;
  }

  public static searchArray(array:any[],item:any):boolean{
    for(let i in array){
      if(item == array[i])
        return true;
    }
    return false;
  }

}
