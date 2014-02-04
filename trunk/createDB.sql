-- ----------------------------
--  Table1 structure for help-seeking video and experiment 
-- ----------------------------
-- 
--  视频编号		视频文件名		实验者	       		任务名		 		 时长				实验日期				备注
-- videoID	videoFileName	experimenter	taskName	videoLength  experimentDate 		 memo
--   1   7_1_jiangqingtao.lxe  宋扬			聊天软件开发   		1:27:03     2012-10-19 14:30:00		视频文件提交出错等
create table if not exists helpseekingvideo
(	
	videoID 		 	INTEGER,
	videoFileName 		varchar(254),
	experimenter  		varchar(100),
	taskName    		varchar(100),
	videoLength			varchar(8),
	experimentDate 		varchar(20),
	memo      			varchar(1000)
);	
