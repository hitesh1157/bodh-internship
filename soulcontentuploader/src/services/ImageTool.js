export default class ImageTool {
    static _toCroppedFile(dataURI, file) {
        // convert base64/URLEncoded data component to raw binary data held in a string
        let byteString;
        if (dataURI.split(',')[0].indexOf('base64') >= 0) {
            byteString = atob(dataURI.split(',')[1]);
        } else {
            byteString = unescape(dataURI.split(',')[1]);
        }
        // separate out the mime component
        let mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
        
        // write the bytes of the string to a typed array
        let ia = new Uint8Array(byteString.length);

        for (let i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        let croppedFile = new File([new Blob([ia], {type:mimeString})], file.name, {type: file.type, lastModified: file.lastModified});
        croppedFile.preview = URL.createObjectURL(croppedFile);
        return croppedFile;
    }
}
