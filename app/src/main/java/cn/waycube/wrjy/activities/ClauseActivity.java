package cn.waycube.wrjy.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.waycube.wrjy.R;
import cn.waycube.wrjy.base.BaseActivity;
import cn.waycube.wrjy.utils.KeyUtil;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ClauseActivity extends BaseActivity {

    @Bind(R.id.wb_clause)
    WebView wbClause;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    //判断是那个服务条款还是隐私保护
    private String id;
    //服务条款
    private String content = "<p style=\"text-align:center\"><br/></p><p style=\"text-align:center\"><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 21px\">服 务 协 议</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">请您认真阅读并充分理解《围绕教育服务协议》（下称</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">“</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">本协议</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">”</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">）。您安装、注册、登录、使用</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">“</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">围绕教育</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">”</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">等行为将视为对本协议的接受，并同意接受本协议各项条款的约束。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">本协议是您（下称</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">“</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">”</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">）与成都市乐符科技有限公司</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">下称</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">“</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">乐符科技</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">”</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">之间关于用户安装、注册、登录、使用</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">“</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">围绕教育</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">”</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">服务（下称</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">“</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">本服务</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">”</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">）所订立的协议。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">一、注册信息和隐私保护</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">1.1 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户在使用本服务时，可能需要提供一些必要的能够对用户进行个人辨识或涉及个人通信的信息，用户应按照要求提供及时、详尽及准确的个人资料，并不断更新个人资料。因个人信息不真实而引起的风险由用户自行承担。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">1.2 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">围绕教育帐号的所有权归乐符科技所有。用户不应将其帐号、密码转让、出售或出借予他人使用，若用户授权他人使用账户，应对被授权人在该账户下发生所有行为负全部责任。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">1.3 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">保护用户个人隐私信息是乐符科技的一贯制度，乐符科技将会采取合理的措施保护用户的个人隐私信息。具体详见《隐私权保护声明》 。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">二、提供服务</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">2.1 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户使用本服务需要下载围绕教育客户端软件。乐符科技发放给用户不可转让及非排他性的许可用于下载软件（围绕教育网页版功能需要通过二维码扫描登录）。用户仅可为访问或使用本服务的目的而使用这些软件及服务。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">2.2 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">乐符科技保留因业务发展需要，单方面对本服务的全部或部分服务内容在任何时候不经任何通知的情况下变更、暂停、限制、终止或撤销的权利，用户需承担此风险。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">三、使用规则</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">3.1 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户在使用本服务时，必须遵守中华人民共和国相关法律法规的规定，不得利用本服务进行任何违法或不正当的活动，包括但不限于制作、上载、展示、张贴、传播或以其它方式传送如下内容：</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(1) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">反对宪法所确定的基本原则的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(2) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(3) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">损害国家荣誉和利益的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(4) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">煽动民族仇恨、民族歧视、破坏民族团结的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(5) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">破坏国家宗教政策，宣扬邪教和封建迷信的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(6) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">散布谣言，扰乱社会秩序，破坏社会稳定的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(7) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(8) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">侵害他人名誉权、肖像权、知识产权、商业秘密等合法权利的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(9) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">含有任何性或性暗示、骚扰、辱骂、恐吓、威胁、侵害他人隐私等恶意信息的；</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">(10) </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">含有法律、行政法规禁止的其他内容的信息。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">3.2 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户充分了解并同意对自己账号下的一切行为负责，包括但不限于用户所传送的任何内容以及由此产生的任何结果。用户承担法律责任的形式包括但不限于：对遭受侵害者进行赔偿，以及在乐符科技首先承担了因用户行为导致的行政处罚或侵权损害赔偿责任后，用户给予乐符科技等额的赔偿。乐符科技有权视用户的行为性质，采取包括但不限于删除用户发布信息内容、暂停使用许可、终止服务、限制使用、回收围绕教育帐号、追究法律责任等措施。对恶意注册围绕教育帐号或利用围绕教育帐号进行违法活动或其他违反本协议的行为，乐符科技有权回收其帐号。同时，乐符科技会视司法部门的要求，协助调查。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">3.3 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户不得对本服务任何部分或本服务之使用或获得，进行复制、拷贝、出售、转售或用于任何其它商业目的。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">四、免责及责任限制</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">4.1 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户因互联网网络故障、计算机病毒、黑客攻击、系统不稳定、电信部门的通讯线路故障及其他各种不可抗力原因而遭受的一切损失，用户须自行承担，乐符科技不承担任何责任。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">4.2 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户违反本协议或相关的服务条款的规定，导致或产生的任何第三方主张的任何索赔、要求或损失，包括合理的律师费，用户同意赔偿乐符科技，并使之免受损害。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">4.3 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">用户须了解，在使用本服务过程中存在有来自他人的包括威胁性的、诽谤性的、令人反感的非法的内容或行为，或对他人权利的侵犯（包括知识产权）的匿名或冒名的信息的风险，用户须承担以上风险，乐符科技对本服务不作任何类型的担保。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">4.4 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">维护软件安全与正常使用是乐符科技和用户的共同责任，乐符科技将按照行业标准合理审慎地采取必要技术措施保护用户的终端设备信息和数据安全，但是用户承认和同意乐符科技并不能就此提供完全保证。乐符科技不对用户在本服务中相关数据的删除或存储失败负责。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">4.5</span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">在任何情况下，乐符科技均不对间接性、后果性、惩罚性、偶然性、特殊性或刑罚性的损害承担责任，乐符科技对用户承担的全部责任，无论因何原因或何种行为方式，始终不超过用户因使用本服务而支付给乐符科技的费用。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">五、权利声明</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">本服务涉及的一切知识产权，以及与本服务相关的所有信息内容，包括但不限于：文字表述及其组合、图标、图饰、图表、色彩、界面设计、版面框架、有关数据、印刷材料、电子文档等均为乐符科技所有（涉及第三方授权的除外），受中华人民共和国著作权法、商标法、专利法、反不正当竞争法和相应的国际条约以及知识产权法律法规的保护。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">六、其他</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">6.1 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">乐符科技郑重提醒用户注意本协议中免除乐符科技责任的条款，请用户仔细阅读，自主考虑风险。乐符科技有权根据互联网的发展、中华人民共和国有关法律法规的变化以及公司经营状况和经营策略的调整，不断地完善本服务质量并依此修改本协议条款。更新后的协议条款一旦公布即代替原来的协议条款，用户可通过网页查阅最新版协议条款。如果用户不接受修改后的条款，请立即停止使用本服务，用户继续使用本服务将被视为接受修改后的协议。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">6.2 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">本协议的效力、解释及纠纷的解决，适用于中华人民共和国法律。若用户和乐符科技之间发生任何纠纷或争议，首先应友好协商解决，协商不成的，用户同意将纠纷或争议提交乐符科技所在地有管辖权的人民法院管辖。</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">&nbsp;</span></p><p><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">6.3 </span><span style=\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\">以上各项条款内容的最终解释权及修改权归乐符科技所有。</span></p>";


    //隐私保护协议
    private String contenttwo = "<p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: Helvetica;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p style=\\\"text-align:center\\\"><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 21px\\\">&nbsp;</span></p><p style=\\\"text-align:center\\\"><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 21px\\\">隐私权保护声明</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">成都市乐符科技有限公司（下称</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">“</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">乐符科技</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">”</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">）非常重视个人隐私信息的保护，在使用乐符科技提供的产品和服务前，请您（下称</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">“</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">用户</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">”</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">）务必仔细阅读并透彻理解《隐私权保护声明》（下称</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">“</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">本声明</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">”</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">）。一旦用户选择使用，即表示认可并接受本声明现有内容及其可能随时更新的内容。</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">本声明解释了用户个人隐私信息收集和使用的有关情况，本声明适用于乐符科技的所有相关产品和服务。</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">1</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">、用户在使用乐符科技提供的产品和服务时，可能需要提供一些必要的能够对用户进行个人辨识或涉及个人通信的信息，包括但不限于用户真实姓名、手机号码、所在地理位置信息等。如果用户无法提供此类信息，可能无法使用对应服务或在使用过程中受到限制。同时，为了运营和改善乐符科技的技术和服务，乐符科技可能会自行收集使用或向第三方提供用户对服务的操作状态以及使用习惯等信息和其他一切个人隐私信息范围外的普通信息，以改善用户体验。</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">2</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">、一般情况下，用户可随时浏览、修改自己提交的信息，但出于安全性和身份识别的考虑，用户可能无法修改注册时提供的初始注册信息及其他验证信息。</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">3</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">、保护用户个人隐私信息是乐符科技的一贯制度，乐符科技将会采取合理的措施保护用户个人隐私信息。乐符科技未经用户同意，不向任何第三方公开、透露用户个人隐私信息。但以下特定情形造成的用户个人隐私信息泄露由用户自行承担：</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">&nbsp;</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">(1) </span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">乐符科技根据法律法规规定或有权机关的指示提供用户的个人隐私信息；</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">(2) </span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">由于用户将其用户帐号密码告知他人或与他人共享注册帐号与密码等非乐符科技原因导致的任何个人隐私信息的泄漏；</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">(3) </span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">用户自行向第三方公开其个人隐私信息；</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">(4) </span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">用户与乐符科技及合作单位之间就用户个人隐私信息的使用公开达成约定，乐符科技因此向合作单位公开用户个人隐私信息；</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">(5) </span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">任何由于黑客攻击、电脑病毒侵入及其他不可抗力事件导致用户个人隐私信息的泄露。</span></p><p><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">4</span><span style=\\\"font-family: 微软雅黑;letter-spacing: 0;font-size: 15px\\\">、乐符科技郑重提醒用户注意本声明中免除乐符科技责任的条款，请用户仔细阅读，自主考虑风险。以上各项条款内容的最终解释权及修改权归乐符科技所有。</span></p><p><br/></p>";


    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {

        id = getIntent().getStringExtra(KeyUtil.KEY_ONE);

        if (id.equals("1")) {
            tvTitle.setText("服务协议");
            Document doc_Dis = Jsoup.parse(content);
            Elements ele_Img = doc_Dis.getElementsByTag("img");
            if (ele_Img.size() != 0) {
                for (Element e_Img : ele_Img) {
                    e_Img.attr("style", "width:100%");
                }
            }
            String newHtmlContent = doc_Dis.toString();
            wbClause.loadDataWithBaseURL(null, newHtmlContent, "text/html", "utf-8", null);
        }else {
            tvTitle.setText("隐私权保护声明");
            Document doc_Dis = Jsoup.parse(contenttwo);
            Elements ele_Img = doc_Dis.getElementsByTag("img");
            if (ele_Img.size() != 0) {
                for (Element e_Img : ele_Img) {
                    e_Img.attr("style", "width:100%");
                }
            }
            String newHtmlContent = doc_Dis.toString();
            wbClause.loadDataWithBaseURL(null, newHtmlContent, "text/html", "utf-8", null);
        }


    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_clause);
    }

}
